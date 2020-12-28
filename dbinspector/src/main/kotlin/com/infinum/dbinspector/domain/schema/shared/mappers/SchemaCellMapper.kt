package com.infinum.dbinspector.domain.schema.shared.mappers

import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.data.models.local.cursor.Field
import com.infinum.dbinspector.data.models.local.cursor.FieldType
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.schema.shared.models.ImageType
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.extensions.toBase64String
import com.infinum.dbinspector.extensions.toHexString
import com.infinum.dbinspector.extensions.toPlaceHolder
import com.infinum.dbinspector.extensions.toUtf8String

internal class SchemaCellMapper : Mappers.SchemaCell {

    override fun toDomain(field: Field): Cell =
        Cell(
            text = when (field.type) {
                FieldType.BLOB -> {
                    field.data?.let { bytes ->
                        when (field.blobPreviewType) {
                            BlobPreviewType.UTF_8 -> bytes.toUtf8String()
                            BlobPreviewType.PLACEHOLDER -> bytes.toPlaceHolder()
                            BlobPreviewType.HEX -> bytes.toHexString()
                            BlobPreviewType.BASE_64 -> bytes.toBase64String()
                            BlobPreviewType.UNSUPPORTED -> bytes.toUtf8String()
                        }
                    } ?: field.text
                }
                else -> field.text
            },
            data = field.data,
            imageType = field.data?.let { bytes -> ImageType(bytes.toHexString()) }
                ?: ImageType.UNSUPPORTED
        )
}
