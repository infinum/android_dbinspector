package com.infinum.dbinspector.data.interactors.history

import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.data.Interactors

internal class ClearHistoryInteractor(
    private val dataStore: com.infinum.dbinspector.data.Sources.Local.History
) : Interactors.ClearHistory {

    override suspend fun invoke(input: HistoryTask) {
        dataStore.store().updateData { value: HistoryEntity ->
            value.toBuilder()
                .clearExecutions()
                .addAllExecutions(value.executionsList.filterNot { it.databasePath == input.databasePath })
                .build()
        }
    }
}
