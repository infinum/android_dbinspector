package com.infinum.dbinspector.domain.history.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Interactors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetHistoryInteractor(
    private val dataStore: Sources.Local.History
) : Interactors.GetHistory {

    override fun invoke(input: HistoryTask): Flow<HistoryEntity> =
        dataStore.flow().map { value: HistoryEntity ->
            value.toBuilder()
                .clearExecutions()
                .addAllExecutions(value.executionsList.filter { it.databasePath == input.databasePath })
                .build()
        }
}
