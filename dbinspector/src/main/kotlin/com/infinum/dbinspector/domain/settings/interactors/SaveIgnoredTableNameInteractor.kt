package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Interactors

internal class SaveIgnoredTableNameInteractor(
    private val dataStore: Sources.Local.Store
) : Interactors.SaveIgnoredTableName {

    override suspend fun invoke(input: SettingsTask) {
        dataStore.settings().updateData {
            it.toBuilder()
                .addIgnoredTableNames(
                    0,
                    SettingsEntity.IgnoredTableName.newBuilder()
                        .setName(input.ignoredTableName)
                        .build()
                )
                .build()
        }
    }
}
