package com.infinum.dbinspector.web.extensions

import java.io.InputStream

internal fun InputStream.readAll() =
    this.bufferedReader().use { it.readText() }
