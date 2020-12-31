package com.infinum.dbinspector.extensions

import java.util.Locale

fun String.uppercase() = this.toUpperCase(Locale.getDefault())

fun String.lowercase() = this.toLowerCase(Locale.getDefault())
