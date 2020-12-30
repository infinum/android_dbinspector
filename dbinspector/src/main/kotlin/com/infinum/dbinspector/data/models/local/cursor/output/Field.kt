package com.infinum.dbinspector.data.models.local.cursor.output

import com.infinum.dbinspector.data.models.local.proto.SettingsEntity

internal data class Field(
    val type: FieldType,
    val text: String? = null,
    val data: ByteArray? = null,
    val blobPreview: SettingsEntity.BlobPreviewMode = SettingsEntity.BlobPreviewMode.UNRECOGNIZED
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Field

        if (type != other.type) return false
        if (text != other.text) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false
        if (blobPreview != other.blobPreview) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + (data?.contentHashCode() ?: 0)
        result = 31 * result + blobPreview.hashCode()
        return result
    }
}