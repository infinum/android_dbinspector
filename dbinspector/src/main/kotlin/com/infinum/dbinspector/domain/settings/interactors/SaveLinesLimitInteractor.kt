package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.domain.Interactors

internal class SaveLinesLimitInteractor(
    private val dataStore: Sources.Local.Store
) : Interactors.SaveLinesLimit {

    override suspend fun invoke(input: Boolean) {
        dataStore.settings().updateData {
            it.toBuilder().setLinesLimit(input).build()
        }
    }
}
