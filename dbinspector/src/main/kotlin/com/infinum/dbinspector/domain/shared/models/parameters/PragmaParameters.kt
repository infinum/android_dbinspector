package com.infinum.dbinspector.domain.shared.models.parameters

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.domain.shared.base.PageParameters

internal sealed class PragmaParameters(override var page: Int?) : PageParameters(page) {

    data class Version(
        val databasePath: String,
        val database: SQLiteDatabase? = null,
        val statement: String,
        override var page: Int? = null
    ) : PragmaParameters(page)

    data class Pragma(
        val databasePath: String,
        val database: SQLiteDatabase? = null,
        val statement: String,
        val sort: SortParameters = SortParameters(),
        override var page: Int? = Domain.Constants.Limits.INITIAL_PAGE,
        val pageSize: Int = Domain.Constants.Limits.PAGE_SIZE
    ) : PragmaParameters(page)
}
