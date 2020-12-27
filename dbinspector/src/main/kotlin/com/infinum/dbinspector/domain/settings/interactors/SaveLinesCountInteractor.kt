package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.domain.Interactors

internal class SaveLinesCountInteractor(
    private val dataStore: Sources.Local.Store
) : Interactors.SaveLinesCount {

    override suspend fun invoke(input: Int) {
        dataStore.settings().updateData {
            it.toBuilder().setLinesCount(input).build()
        }
    }
}
