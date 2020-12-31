package com.infinum.dbinspector.domain.shared.models.parameters

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.domain.shared.base.BaseParameters

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
        val page: Int? = Domain.Constants.Limits.INITIAL_PAGE,
        val pageSize: Int = Domain.Constants.Limits.PAGE_SIZE
    ) : PragmaParameters()

    data class ForeignKeys(
        val databasePath: String,
        val database: SQLiteDatabase? = null,
        val statement: String,
        val sort: SortParameters = SortParameters(),
        val page: Int? = Domain.Constants.Limits.INITIAL_PAGE,
        val pageSize: Int = Domain.Constants.Limits.PAGE_SIZE
    ) : PragmaParameters()

    data class Indexes(
        val databasePath: String,
        val database: SQLiteDatabase? = null,
        val statement: String,
        val sort: SortParameters = SortParameters(),
        val page: Int? = Domain.Constants.Limits.INITIAL_PAGE,
        val pageSize: Int = Domain.Constants.Limits.PAGE_SIZE
    ) : PragmaParameters()
}
