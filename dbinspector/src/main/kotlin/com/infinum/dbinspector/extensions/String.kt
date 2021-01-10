package com.infinum.dbinspector.extensions

import java.util.Locale

internal fun String.uppercase() = this.toUpperCase(Locale.getDefault())

internal fun String.lowercase() = this.toLowerCase(Locale.getDefault())
