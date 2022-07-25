package com.infinum.dbinspector.data.interactors.history

import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.data.Interactors

internal class GetExecutionInteractor(
    private val dataStore: com.infinum.dbinspector.data.Sources.Local.History,
    private val distance: com.infinum.dbinspector.data.Sources.Memory.Distance
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
