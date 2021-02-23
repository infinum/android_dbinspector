package com.infinum.dbinspector.domain.history.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Interactors
import me.xdrop.fuzzywuzzy.FuzzySearch

internal class GetExecutionInteractor(
    private val dataStore: Sources.Local.History
) : Interactors.GetExecution {

    override suspend fun invoke(input: HistoryTask): HistoryEntity =
        dataStore.current()
            .executionsList
            .filter { it.databasePath == input.execution?.databasePath }
            .let { entities ->
                FuzzySearch.extractOne(
                    input.execution?.execution.orEmpty(),
                    entities.map { it.execution }
                )
                    ?.takeIf { it.score != 0 }
                    ?.let { entities[it.index] }
            }
            ?.let {
                HistoryEntity.getDefaultInstance()
                    .toBuilder()
                    .addExecutions(it)
                    .build()
            } ?: HistoryEntity.getDefaultInstance()
}
