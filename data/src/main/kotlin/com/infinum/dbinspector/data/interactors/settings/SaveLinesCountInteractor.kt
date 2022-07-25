package com.infinum.dbinspector.data.interactors.settings

import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.Interactors

internal class SaveLinesCountInteractor(
    private val dataStore: com.infinum.dbinspector.data.Sources.Local.Settings
) : Interactors.SaveLinesCount {

    override suspend fun invoke(input: SettingsTask) {
        input.linesCount.takeIf { it >= 0 }?.let {
            dataStore.store().updateData {
                it.toBuilder().setLinesCount(input.linesCount).build()
            }
        } ?: throw IllegalArgumentException("Cannot set a negative number for lines count.")
    }
}
