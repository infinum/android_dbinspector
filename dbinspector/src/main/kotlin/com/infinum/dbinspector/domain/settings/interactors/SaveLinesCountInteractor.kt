package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.domain.Interactors

internal class SaveLinesCountInteractor(
    private val dataStore: Sources.Local.Settings
) : Interactors.SaveLinesCount {

    override suspend fun invoke(input: SettingsTask) {
        input.linesCount.takeIf { it >= 0 }?.let {
            dataStore.store().updateData {
                it.toBuilder().setLinesCount(input.linesCount).build()
            }
        } ?: throw IllegalArgumentException("Cannot set a negative number for lines count.")
    }
}
