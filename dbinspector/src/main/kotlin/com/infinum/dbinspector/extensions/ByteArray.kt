package com.infinum.dbinspector.extensions

import com.infinum.dbinspector.ui.Presentation
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.util.zip.Adler32

private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

@Suppress("unused")
internal fun ByteArray.toPlaceHolder(): String =
    Presentation.Constants.Settings.BLOB_DATA_PLACEHOLDER

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

@Suppress("MagicNumber")
internal fun ByteArray.toBase64String(): String {
    val table =
        (CharRange('A', 'Z') + CharRange('a', 'z') + CharRange('0', '9') + '+' + '/').toCharArray()
    val output = ByteArrayOutputStream()
    var padding = 0
    var position = 0
    while (position < this.size) {
        var b = this[position].toInt() and 0xFF shl 16 and 0xFFFFFF
        if (position + 1 < this.size) b =
            b or (this[position + 1].toInt() and 0xFF shl 8) else padding++
        if (position + 2 < this.size) b = b or (this[position + 2].toInt() and 0xFF) else padding++
        (0 until 4 - padding).forEach { _ ->
            val c = b and 0xFC0000 shr 18
            output.write(table[c].code)
            b = b shl 6
        }
        position += 3
    }
    (0 until padding).forEach { _ ->
        output.write('='.code)
    }
    return String(output.toByteArray())
}

internal fun ByteArray.toChecksum(): String =
    Adler32().apply {
        this.update(this@toChecksum)
    }.value.toString()

internal fun ByteArray.toSha1(): String =
    MessageDigest.getInstance("SHA-1")
        .digest(this)
        .joinToString("") { hex -> "%02x".format(hex) }
