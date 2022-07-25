package com.infinum.dbinspector.data.interactors.settings

import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.data.Interactors

internal class SaveIgnoredTableNameInteractor(
    private val dataStore: com.infinum.dbinspector.data.Sources.Local.Settings
) : Interactors.SaveIgnoredTableName {

    override suspend fun invoke(input: SettingsTask) {
        input.ignoredTableName.takeIf { it.isNotBlank() }?.let { name ->
            dataStore.store().updateData { entity ->
                entity.toBuilder()
                    .addIgnoredTableNames(
                        0,
                        SettingsEntity.IgnoredTableName.newBuilder()
                            .setName(name)
                            .build()
                    )
                    .build()
            }
        } ?: throw IllegalStateException("Ignored table name cannot be empty or blank.")
    }
}
