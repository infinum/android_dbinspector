package com.infinum.dbinspector.data.models.local.cursor.output

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity

public data class Field(
    val type: FieldType,
    val text: String? = null,
    val blob: ByteArray? = null,
    val linesCount: Int = Int.MAX_VALUE,
    val truncate: SettingsEntity.TruncateMode = SettingsEntity.TruncateMode.UNRECOGNIZED,
    val blobPreview: SettingsEntity.BlobPreviewMode = SettingsEntity.BlobPreviewMode.UNRECOGNIZED
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Field

        if (type != other.type) return false
        if (text != other.text) return false
        if (blob != null) {
            if (other.blob == null) return false
            if (!blob.contentEquals(other.blob)) return false
        } else if (other.blob != null) return false
        if (linesCount != other.linesCount) return false
        if (truncate != other.truncate) return false
        if (blobPreview != other.blobPreview) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + (blob?.contentHashCode() ?: 0)
        result = 31 * result + linesCount
        result = 31 * result + truncate.hashCode()
        result = 31 * result + blobPreview.hashCode()
        return result
    }
}
