package com.infinum.dbinspector.extensions

import android.util.Base64
import com.infinum.dbinspector.ui.shared.Constants

private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

internal fun ByteArray.toPlaceHolder(): String =
    Constants.Settings.BLOB_DATA_PLACEHOLDER

internal fun ByteArray.toUtf8String(): String =
    String(this, Charsets.UTF_8)

@Suppress("MagicNumber")
internal fun ByteArray.toHexString(): String {
    val buffer = StringBuffer()
    this.forEach { byte ->
        val octet = byte.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        buffer.append(HEX_ARRAY[firstIndex])
        buffer.append(HEX_ARRAY[secondIndex])
    }
    return buffer.toString()
}

internal fun ByteArray.toBase64String(): String =
    Base64.encodeToString(this, Base64.NO_WRAP)
