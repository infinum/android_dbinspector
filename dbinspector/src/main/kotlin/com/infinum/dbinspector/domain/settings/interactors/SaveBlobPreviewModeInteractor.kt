package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.Interactors

internal class SaveBlobPreviewModeInteractor(
    private val dataStore: Sources.Local.Store
) : Interactors.SaveBlobPreviewMode {

    override suspend fun invoke(input: BlobPreviewType) {
        dataStore.settings().updateData {
            it.toBuilder().setBlobPreview(
                when (input) {
                    BlobPreviewType.PLACEHOLDER -> SettingsEntity.BlobPreviewMode.PLACEHOLDER
                    BlobPreviewType.UTF_8 -> SettingsEntity.BlobPreviewMode.UTF8
                    BlobPreviewType.HEX -> SettingsEntity.BlobPreviewMode.HEX
                    BlobPreviewType.BASE_64 -> SettingsEntity.BlobPreviewMode.BASE64
                    else -> SettingsEntity.BlobPreviewMode.PLACEHOLDER
                }
            ).build()
        }
    }
}
