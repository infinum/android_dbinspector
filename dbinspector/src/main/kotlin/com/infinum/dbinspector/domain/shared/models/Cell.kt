package com.infinum.dbinspector.domain.shared.models

import com.infinum.dbinspector.domain.schema.shared.models.ImageType

internal data class Cell(
    val text: String? = null,
    val data: ByteArray? = null,
    val imageType: ImageType = ImageType.UNSUPPORTED,
    val isExpandable: Boolean = false,
    val isExpanded: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cell

        if (text != other.text) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false
        if (imageType != other.imageType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text?.hashCode() ?: 0
        result = 31 * result + (data?.contentHashCode() ?: 0)
        result = 31 * result + imageType.hashCode()
        return result
    }
}
