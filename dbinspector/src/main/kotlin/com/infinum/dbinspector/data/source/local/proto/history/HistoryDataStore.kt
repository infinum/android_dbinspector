package com.infinum.dbinspector.data.source.local.proto.history

import androidx.datastore.core.DataStore
import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

internal class HistoryDataStore(
    private val store: DataStore<HistoryEntity>
) : Sources.Local.History {

    override suspend fun store(): DataStore<HistoryEntity> = store

    override suspend fun current(): HistoryEntity =
        store.data.firstOrNull() ?: HistoryEntity.getDefaultInstance()

    override fun flow(): Flow<HistoryEntity> =
        store.data
}
