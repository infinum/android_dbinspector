package com.infinum.dbinspector.data.models.raw

public data class DatabaseDescriptorTask(
    val exists: Boolean,
    val parentPath: String,
    val name: String,
    val extension: String = "",
    val version: String = ""
) {
    val absolutePath: String
        get() = if (extension.isEmpty()) "$parentPath/$name" else "$parentPath/$name.$extension"
}
