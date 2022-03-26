package com.infinum.dbinspector.domain.history.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Interactors

internal class GetExecutionInteractor(
    private val dataStore: Sources.Local.History,
    private val distance: Sources.Memory.Distance
) : Interactors.GetExecution {

    override suspend fun invoke(input: HistoryTask): HistoryEntity =
        dataStore.current()
            .executionsList
            .filter { it.databasePath == input.execution?.databasePath }
            .takeIf { it.isNotEmpty() }
            ?.let { entities: List<HistoryEntity.ExecutionEntity> ->
                distance.calculate(
                    input.execution?.execution.orEmpty(),
                    entities.map { it.execution },
                )?.let { index -> entities[index] }
            }
            ?.let {
                HistoryEntity.getDefaultInstance()
                    .toBuilder()
                    .addExecutions(it)
                    .build()
            } ?: HistoryEntity.getDefaultInstance()
}
