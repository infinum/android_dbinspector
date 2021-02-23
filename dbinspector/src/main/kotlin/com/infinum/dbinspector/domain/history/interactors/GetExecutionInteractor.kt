package com.infinum.dbinspector.domain.history.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.domain.Interactors

internal class GetExecutionInteractor(
    private val dataStore: Sources.Local.History
) : Interactors.GetExecution {

    override suspend fun invoke(input: HistoryTask): HistoryEntity =
        dataStore.current()
            .executionsList
            .filter { it.databasePath == input.execution?.databasePath }
            .map { it to Domain.Algorithms.levenshtein(it.execution, input.execution?.execution.orEmpty()) }
            .minByOrNull { it.second }
            ?.first
            ?.let {
                HistoryEntity.getDefaultInstance()
                    .toBuilder()
                    .addExecutions(it)
                    .build()
            } ?: HistoryEntity.getDefaultInstance()
}
