package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode

internal class BlobPreviewModeMapper : Mappers.BlobPreviewMode {

    override suspend fun invoke(model: SettingsEntity.BlobPreviewMode): BlobPreviewMode =
        when (model) {
            SettingsEntity.BlobPreviewMode.PLACEHOLDER -> BlobPreviewMode.PLACEHOLDER
            SettingsEntity.BlobPreviewMode.UTF8 -> BlobPreviewMode.UTF_8
            SettingsEntity.BlobPreviewMode.HEX -> BlobPreviewMode.HEX
            SettingsEntity.BlobPreviewMode.BASE64 -> BlobPreviewMode.BASE_64
            else -> BlobPreviewMode.PLACEHOLDER
        }
}
