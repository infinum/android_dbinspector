package com.infinum.dbinspector.domain.schema.shared.mappers

import android.util.Base64
import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.data.models.local.cursor.Field
import com.infinum.dbinspector.data.models.local.cursor.FieldType
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.schema.shared.models.ImageType
import com.infinum.dbinspector.domain.shared.models.Cell

internal class SchemaCellMapper : Mappers.SchemaCell {

    override fun toDomain(field: Field): Cell =
        Cell(
            text = when (field.type) {
                FieldType.BLOB -> {
                    field.data?.let { bytes ->
                        // TODO: These are really bad and really slow
                        when (field.blobPreviewType) {
                            BlobPreviewType.UTF_8 -> String(bytes, Charsets.UTF_8)
                            BlobPreviewType.PLACEHOLDER -> "[ DATA ]"
                            BlobPreviewType.HEX -> bytes.joinToString("") { "%02x".format(it) }
                            BlobPreviewType.BASE_64 -> Base64.encodeToString(bytes, Base64.NO_WRAP)
                            BlobPreviewType.UNSUPPORTED -> String(bytes, Charsets.UTF_8)
                        }
                    } ?: field.text
                }
                else -> field.text
            },
            data = field.data,
            imageType = field.data?.let { bytes ->
                ImageType(bytes.joinToString("") { "%02x".format(it) })
            } ?: ImageType.UNSUPPORTED
        )
}
