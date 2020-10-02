package im.dino.dbinspector.extensions

import android.content.Context

internal val Context.databaseDir: String
    get() = "${filesDir.path.substring(0, filesDir.path.lastIndexOf("/"))}/databases"
