package com.infinum.dbinspector.data.interactors.settings

import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.Interactors

internal class SaveTruncateModeInteractor(
    private val dataStore: com.infinum.dbinspector.data.Sources.Local.Settings
) : Interactors.SaveTruncateMode {

    override suspend fun invoke(input: SettingsTask) {
        dataStore.store().updateData {
            it.toBuilder().setTruncateMode(input.truncateMode).build()
        }
    }
}
