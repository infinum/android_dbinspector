package com.infinum.dbinspector.data.source.local.proto.settings

import androidx.datastore.core.DataStore
import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

internal class SettingsDataStore(
    private val store: DataStore<SettingsEntity>
) : Sources.Local.Settings {

    override suspend fun store(): DataStore<SettingsEntity> = store

    override suspend fun current(): SettingsEntity =
        store.data.firstOrNull() ?: SettingsEntity.getDefaultInstance()

    override fun flow(): Flow<SettingsEntity> =
        store.data
}
