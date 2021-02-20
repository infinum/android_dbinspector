package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Interactors
import kotlinx.coroutines.flow.firstOrNull

internal class RemoveIgnoredTableNameInteractor(
    private val dataStore: Sources.Local.Store<SettingsEntity>
) : Interactors.RemoveIgnoredTableName {

    override suspend fun invoke(input: SettingsTask) {
        dataStore.store()
            .data
            .firstOrNull()
            ?.ignoredTableNamesList
            ?.mapIndexed { index, ignoredTableName -> index to ignoredTableName }
            ?.find { it.second.name == input.ignoredTableName }
            ?.let { indexed ->
                dataStore.store().updateData {
                    it.toBuilder().removeIgnoredTableNames(indexed.first).build()
                }
            }
    }
}
