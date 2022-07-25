package com.infinum.dbinspector.data.models.local.cursor.input

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.data.Data

public data class Query(
    val databasePath: String = "",
    val database: SQLiteDatabase? = null,
    val statement: String,
    val order: Order = Order.ASCENDING,
    val pageSize: Int = Data.Constants.Limits.PAGE_SIZE,
    val page: Int? = Data.Constants.Limits.INITIAL_PAGE
)
