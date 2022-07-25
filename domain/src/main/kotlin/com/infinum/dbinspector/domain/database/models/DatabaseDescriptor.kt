package com.infinum.dbinspector.domain.database.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
public data class DatabaseDescriptor(
    val exists: Boolean,
    val parentPath: String,
    val name: String,
    val extension: String = "",
    val version: String = ""
) : Parcelable {
    val absolutePath: String
        get() = if (extension.isEmpty()) "$parentPath/$name" else "$parentPath/$name.$extension"
}
