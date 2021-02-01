package com.infinum.dbinspector.domain.shared.models.parameters

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.domain.shared.base.PageParameters
import com.infinum.dbinspector.domain.shared.models.Sort

internal data class ContentParameters(
    val databasePath: String = "",
    val database: SQLiteDatabase? = null,
    val statement: String,
    val sort: SortParameters = SortParameters(Sort.ASCENDING),
    val pageSize: Int = Domain.Constants.Limits.PAGE_SIZE,
    override var page: Int? = Domain.Constants.Limits.INITIAL_PAGE
) : PageParameters(page)
