package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.Interactors

internal class SaveBlobPreviewModeInteractor(
    private val dataStore: Sources.Local.Store
) : Interactors.SaveBlobPreviewMode {

    override suspend fun invoke(input: SettingsEntity.BlobPreviewMode) {
        dataStore.settings().updateData {
            it.toBuilder().setBlobPreview(input).build()
        }
    }
}
