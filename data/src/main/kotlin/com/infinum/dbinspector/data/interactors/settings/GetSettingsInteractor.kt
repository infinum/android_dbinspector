package com.infinum.dbinspector.data.interactors.settings

import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.data.Interactors

internal class GetSettingsInteractor(
    private val dataStore: com.infinum.dbinspector.data.Sources.Local.Settings
) : Interactors.GetSettings {

    override suspend fun invoke(input: SettingsTask): SettingsEntity =
        dataStore.current()
}
