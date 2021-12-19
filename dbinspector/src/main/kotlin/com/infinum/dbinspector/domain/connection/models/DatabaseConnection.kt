package com.infinum.dbinspector.domain.connection.models

import android.database.sqlite.SQLiteDatabase

internal data class DatabaseConnection(
    val database: SQLiteDatabase
)
