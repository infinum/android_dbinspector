package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Interactors

internal class GetSettingsInteractor(
    private val dataStore: Sources.Local.Store
) : Interactors.GetSettings {

    override suspend fun invoke(input: SettingsTask): SettingsEntity =
        dataStore.current()
}
