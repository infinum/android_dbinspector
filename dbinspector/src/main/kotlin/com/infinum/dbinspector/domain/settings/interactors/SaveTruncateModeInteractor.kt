package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.Interactors

internal class SaveTruncateModeInteractor(
    private val dataStore: Sources.Local.Store
) : Interactors.SaveTruncateMode {

    override suspend fun invoke(input: SettingsEntity.TruncateMode) {
        dataStore.settings().updateData {
            it.toBuilder().setTruncateMode(
                input
            ).build()
        }
    }
}
