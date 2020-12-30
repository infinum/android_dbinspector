package com.infinum.dbinspector.domain.shared.converters

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode

internal class BlobPreviewConverter : Converters.BlobPreview {

    override suspend fun invoke(parameters: BlobPreviewMode): SettingsEntity.BlobPreviewMode =
        when (parameters) {
            BlobPreviewMode.UNSUPPORTED -> SettingsEntity.BlobPreviewMode.UNRECOGNIZED
            BlobPreviewMode.PLACEHOLDER -> SettingsEntity.BlobPreviewMode.PLACEHOLDER
            BlobPreviewMode.UTF_8 -> SettingsEntity.BlobPreviewMode.UTF8
            BlobPreviewMode.HEX -> SettingsEntity.BlobPreviewMode.HEX
            BlobPreviewMode.BASE_64 -> SettingsEntity.BlobPreviewMode.BASE64
        }
}
