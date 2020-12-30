package com.infinum.dbinspector.data.models.local.cursor.input

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.ui.shared.Constants

internal data class Query(
    val databasePath: String = "",
    val database: SQLiteDatabase? = null,
    val statement: String,
    val order: Order = Order.ASCENDING,
    val pageSize: Int = Constants.Limits.PAGE_SIZE,
    val page: Int? = Constants.Limits.INITIAL_PAGE,
    val blobPreview: SettingsEntity.BlobPreviewMode = SettingsEntity.BlobPreviewMode.PLACEHOLDER
)
