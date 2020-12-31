package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.domain.Interactors

internal class SaveTruncateModeInteractor(
    private val dataStore: Sources.Local.Store
) : Interactors.SaveTruncateMode {

    override suspend fun invoke(input: SettingsTask) {
        dataStore.settings().updateData {
            it.toBuilder().setTruncateMode(input.truncateMode).build()
        }
    }
}
