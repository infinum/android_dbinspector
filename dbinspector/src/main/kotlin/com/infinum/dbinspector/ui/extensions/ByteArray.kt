package com.infinum.dbinspector.ui.extensions

import java.util.zip.Adler32

internal fun ByteArray.toUtf8String(): String =
    String(this, Charsets.UTF_8)

internal fun ByteArray.toChecksum(): String =
    Adler32().apply {
        this.update(this@toChecksum)
    }.value.toString()
