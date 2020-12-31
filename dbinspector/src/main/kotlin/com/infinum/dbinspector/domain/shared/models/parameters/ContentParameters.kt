package com.infinum.dbinspector.domain.shared.models.parameters

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.ui.shared.Constants

internal data class ContentParameters(
    val databasePath: String = "",
    val database: SQLiteDatabase? = null,
    val statement: String,
    val sort: SortParameters = SortParameters(Sort.ASCENDING),
    val pageSize: Int = Constants.Limits.PAGE_SIZE,
    val page: Int? = Constants.Limits.INITIAL_PAGE
) : BaseParameters
