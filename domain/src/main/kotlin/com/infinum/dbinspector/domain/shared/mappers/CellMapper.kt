package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.extensions.toBase64String
import com.infinum.dbinspector.domain.extensions.toHexString
import com.infinum.dbinspector.domain.extensions.toPlaceHolder
import com.infinum.dbinspector.domain.extensions.toUtf8String
import com.infinum.dbinspector.domain.schema.shared.models.ImageType
import com.infinum.dbinspector.domain.shared.models.Cell

internal class CellMapper(
    private val truncateModeMapper: Mappers.TruncateMode
) : Mappers.Cell {

    override suspend fun invoke(model: Field): Cell =
        Cell(
            text = when (model.type) {
                FieldType.BLOB -> {
                    model.blob?.let { bytes ->
                        when (model.blobPreview) {
                            SettingsEntity.BlobPreviewMode.UTF8 -> bytes.toUtf8String()
                            SettingsEntity.BlobPreviewMode.PLACEHOLDER -> bytes.toPlaceHolder()
                            SettingsEntity.BlobPreviewMode.HEX -> bytes.toHexString()
                            SettingsEntity.BlobPreviewMode.BASE64 -> bytes.toBase64String()
                            SettingsEntity.BlobPreviewMode.UNRECOGNIZED -> bytes.toUtf8String()
                        }
                    } ?: model.text
                }
                else -> model.text
            },
            data = model.blob,
            imageType = model.blob?.let { bytes -> ImageType(bytes.toHexString()) }
                ?: ImageType.UNSUPPORTED,
            linesShown = model.linesCount,
            truncateMode = truncateModeMapper(model.truncate)
        )
}
