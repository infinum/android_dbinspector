package com.infinum.dbinspector.web.extensions

import java.io.File

internal fun File.readAll() =
    this.bufferedReader().use { it.readText() }
