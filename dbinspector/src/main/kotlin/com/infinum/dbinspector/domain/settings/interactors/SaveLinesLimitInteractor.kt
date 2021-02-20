package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Interactors

internal class SaveLinesLimitInteractor(
    private val dataStore: Sources.Local.Store<SettingsEntity>
) : Interactors.SaveLinesLimit {

    override suspend fun invoke(input: SettingsTask) {
        dataStore.store().updateData {
            it.toBuilder().setLinesLimit(input.linesLimited).build()
        }
    }
}
