package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.schema.shared.models.ImageType
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.extensions.toBase64String
import com.infinum.dbinspector.extensions.toHexString
import com.infinum.dbinspector.extensions.toPlaceHolder
import com.infinum.dbinspector.extensions.toUtf8String

internal class CellMapper(
    private val truncateModeMapper: Mappers.TruncateMode
) : Mappers.Cell {

    override suspend fun invoke(model: Field): Cell =
        Cell(
            text = when (model.type) {
                FieldType.BLOB -> {
                    model.data?.let { bytes ->
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
            data = model.data,
            imageType = model.data?.let { bytes -> ImageType(bytes.toHexString()) }
                ?: ImageType.UNSUPPORTED,
            linesShown = model.linesCount,
            truncateMode = truncateModeMapper(model.truncate)
        )
}
