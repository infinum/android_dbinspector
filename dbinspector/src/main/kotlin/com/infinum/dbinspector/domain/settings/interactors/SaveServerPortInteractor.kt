package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.domain.Interactors

internal class SaveServerPortInteractor(
    private val dataStore: Sources.Local.Settings
) : Interactors.SaveServerPort {

    override suspend fun invoke(input: SettingsTask) {
        dataStore.store().updateData {
            it.toBuilder().setServerPort(input.serverPort).build()
        }
    }
}
