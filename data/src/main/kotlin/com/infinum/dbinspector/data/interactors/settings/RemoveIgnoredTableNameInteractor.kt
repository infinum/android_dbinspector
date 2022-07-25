package com.infinum.dbinspector.data.interactors.settings

import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.Interactors
import kotlinx.coroutines.flow.firstOrNull

internal class RemoveIgnoredTableNameInteractor(
    private val dataStore: com.infinum.dbinspector.data.Sources.Local.Settings
) : Interactors.RemoveIgnoredTableName {

    override suspend fun invoke(input: SettingsTask) {
        input.ignoredTableName.takeIf { it.isNotBlank() }?.let { name ->
            dataStore.store()
                .data
                .firstOrNull()
                ?.ignoredTableNamesList
                ?.mapIndexed { index, ignoredTableName -> index to ignoredTableName }
                ?.find { it.second.name == name }
                ?.let { indexed ->
                    dataStore.store().updateData { entity ->
                        entity.toBuilder().removeIgnoredTableNames(indexed.first).build()
                    }
                }
        } ?: throw IllegalStateException("Ignored table name cannot be empty or blank.")
    }
}
