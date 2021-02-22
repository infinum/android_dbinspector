package com.infinum.dbinspector.domain.history.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Interactors

internal class ClearHistoryInteractor(
    private val dataStore: Sources.Local.History
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
