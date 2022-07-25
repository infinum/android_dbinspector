package com.infinum.dbinspector.data.extensions

import android.content.Context
import java.io.File

internal fun Context.dataStoreFile(fileName: String): File =
    File(filesDir, "datastore/$fileName")

internal val Context.databaseDir: String
    get() = "${filesDir.path.substring(0, filesDir.path.lastIndexOf("/"))}/databases"
