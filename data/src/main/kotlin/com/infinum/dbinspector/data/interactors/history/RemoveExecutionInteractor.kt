package com.infinum.dbinspector.data.interactors.history

import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.data.Interactors

internal class RemoveExecutionInteractor(
    private val dataStore: com.infinum.dbinspector.data.Sources.Local.History
) : Interactors.RemoveExecution {

    override suspend fun invoke(input: HistoryTask) {
        input.execution?.let { execution ->
            dataStore.store().updateData { value: HistoryEntity ->
                value.toBuilder()
                    .removeExecutions(
                        value.executionsList.indexOfFirst {
                            it.databasePath == execution.databasePath &&
                                it.execution == execution.execution &&
                                it.timestamp == execution.timestamp &&
                                it.success == execution.success
                        }
                    )
                    .build()
            }
        }
    }
}
