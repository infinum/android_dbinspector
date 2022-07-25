package com.infinum.dbinspector.ui.extensions

import java.util.Locale

internal fun String.uppercase() = this.uppercase(Locale.getDefault())

internal fun String.lowercase() = this.lowercase(Locale.getDefault())
