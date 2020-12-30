package com.infinum.dbinspector.data.source.local.proto

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity

internal class DataStoreFactory(
    context: Context,
    settingsFileName: String,
    settingsSerializer: Serializer<SettingsEntity>
) : Sources.Local.Store {

    private var settings = context.createDataStore(
        fileName = settingsFileName,
        serializer = settingsSerializer
    )

    override suspend fun settings(): DataStore<SettingsEntity> = settings
}
