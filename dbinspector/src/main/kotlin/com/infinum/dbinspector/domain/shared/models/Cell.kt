package com.infinum.dbinspector.domain.shared.models

import android.os.Parcelable
import com.infinum.dbinspector.domain.schema.shared.models.ImageType
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class Cell(
    val text: String? = null,
    val data: ByteArray? = null,
    val imageType: ImageType = ImageType.UNSUPPORTED,
    val linesShown: Int = Int.MAX_VALUE,
    val truncateMode: TruncateMode = TruncateMode.UNKNOWN
) : Parcelable {

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
        if (linesShown != other.linesShown) return false
        if (truncateMode != other.truncateMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text?.hashCode() ?: 0
        result = 31 * result + (data?.contentHashCode() ?: 0)
        result = 31 * result + imageType.hashCode()
        result = 31 * result + linesShown
        result = 31 * result + truncateMode.hashCode()
        return result
    }
}
