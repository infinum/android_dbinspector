package com.infinum.dbinspector.domain.shared.models

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.ui.shared.Constants

internal data class Query(
    val databasePath: String = "",
    val database: SQLiteDatabase? = null,
    val statement: String,
    val order: Direction = Direction.ASCENDING,
    val pageSize: Int = Constants.Limits.PAGE_SIZE,
    val page: Int? = Constants.Limits.INITIAL_PAGE,
    val blobPreviewType: BlobPreviewType = BlobPreviewType.HEX
)
