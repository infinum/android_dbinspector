package com.infinum.dbinspector.domain.history.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Interactors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

internal class GetHistoryInteractor(
    private val dataStore: Sources.Local.History
) : Interactors.GetHistory {

    override suspend fun invoke(input: HistoryTask): Flow<HistoryEntity> =
        dataStore.store().data.transform<HistoryEntity, HistoryEntity> { value: HistoryEntity ->
            this.emit(
                value.toBuilder()
                    .clearExecutions()
                    .addAllExecutions(value.executionsList.filter { it.databasePath == input.databasePath })
                    .build()
            )
        }
}
