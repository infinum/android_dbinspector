package com.infinum.dbinspector.domain.database.models

import android.content.Context
import android.net.Uri

internal class Operation(
    val context: Context,
    val databaseDescriptor: DatabaseDescriptor? = null,
    val importUris: List<Uri> = listOf(),
    val argument: String? = null
)
