package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.Interactors
import kotlinx.coroutines.flow.firstOrNull

internal class LoadSettingsInteractor(
    private val dataStore: Sources.Local.Store
) : Interactors.LoadSettings {

    override suspend fun invoke(input: Unit): SettingsEntity? =
        dataStore.settings().data.firstOrNull()
}
