package com.infinum.dbinspector.data.source.local.proto

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity

class DataStoreFactory(
    context: Context,
    settingsSerializer: Serializer<SettingsEntity>
) : Sources.Local.Store {

    companion object {
        private const val FILENAME = "settings-entity.pb"
    }

    private var settings = context.createDataStore(
        fileName = FILENAME,
        serializer = settingsSerializer)

    override suspend fun settings(): DataStore<SettingsEntity> = settings
}
