package com.infinum.dbinspector.data.models.raw

import android.content.Context
import android.net.Uri

public data class OperationTask(
    val context: Context,
    val databaseDescriptor: DatabaseDescriptorTask? = null,
    val importUris: List<Uri> = listOf(),
    val argument: String? = null
)
