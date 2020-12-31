package com.infinum.dbinspector.domain.shared.models.parameters

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.ui.shared.Constants

internal sealed class PragmaParameters : BaseParameters {

    data class Version(
        val databasePath: String,
        val database: SQLiteDatabase? = null,
        val statement: String
    ) : PragmaParameters()

    data class Info(
        val databasePath: String,
        val database: SQLiteDatabase? = null,
        val statement: String,
        val sort: SortParameters = SortParameters(),
        val page: Int? = Constants.Limits.INITIAL_PAGE,
        val pageSize: Int = Constants.Limits.PAGE_SIZE
    ) : PragmaParameters()

    data class ForeignKeys(
        val databasePath: String,
        val database: SQLiteDatabase? = null,
        val statement: String,
        val sort: SortParameters = SortParameters(),
        val page: Int? = Constants.Limits.INITIAL_PAGE,
        val pageSize: Int = Constants.Limits.PAGE_SIZE
    ) : PragmaParameters()

    data class Indexes(
        val databasePath: String,
        val database: SQLiteDatabase? = null,
        val statement: String,
        val sort: SortParameters = SortParameters(),
        val page: Int? = Constants.Limits.INITIAL_PAGE,
        val pageSize: Int = Constants.Limits.PAGE_SIZE
    ) : PragmaParameters()
}
