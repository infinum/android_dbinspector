package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.TruncateType
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.Interactors

internal class SaveTruncateModeInteractor(
    private val dataStore: Sources.Local.Store
) : Interactors.SaveTruncateMode {

    override suspend fun invoke(input: TruncateType) {
        dataStore.settings().updateData {
            it.toBuilder().setTruncateMode(
                when (input) {
                    TruncateType.START -> SettingsEntity.TruncateMode.START
                    TruncateType.MIDDLE -> SettingsEntity.TruncateMode.MIDDLE
                    TruncateType.END -> SettingsEntity.TruncateMode.END
                }
            ).build()
        }
    }
}
