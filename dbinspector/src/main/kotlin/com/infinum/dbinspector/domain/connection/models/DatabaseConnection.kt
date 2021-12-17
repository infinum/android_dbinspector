package com.infinum.dbinspector.domain.connection.models

import android.database.sqlite.SQLiteDatabase

@JvmInline
internal value class DatabaseConnection(
    val database: SQLiteDatabase
)
