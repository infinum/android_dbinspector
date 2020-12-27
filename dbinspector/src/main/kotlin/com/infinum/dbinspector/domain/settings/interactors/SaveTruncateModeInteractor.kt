package com.infinum.dbinspector.domain.settings.interactors

import android.text.TextUtils
import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.Interactors

internal class SaveTruncateModeInteractor(
    private val dataStore: Sources.Local.Store
) : Interactors.SaveTruncateMode {

    override suspend fun invoke(input: TextUtils.TruncateAt) {
        dataStore.settings().updateData {
            it.toBuilder().setTruncateMode(
                when (input) {
                    TextUtils.TruncateAt.START -> SettingsEntity.TruncateMode.START
                    TextUtils.TruncateAt.MIDDLE -> SettingsEntity.TruncateMode.MIDDLE
                    TextUtils.TruncateAt.END -> SettingsEntity.TruncateMode.END
                    else -> SettingsEntity.TruncateMode.END
                }
            ).build()
        }
    }
}
