package com.infinum.dbinspector.domain.shared.models.parameters

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.pragma.models.TriggerInfoColumns
import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.domain.shared.models.Direction
import com.infinum.dbinspector.ui.shared.Constants
import java.util.*

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
        val columns: List<String> = TriggerInfoColumns.values().map {
            it.name.toLowerCase(Locale.getDefault())
        },
        val order: Direction = Direction.ASCENDING,
        val page: Int? = Constants.Limits.INITIAL_PAGE,
        val pageSize: Int = Constants.Limits.PAGE_SIZE
    ) : PragmaParameters()

    // TODO: Not happy with bloated Info class, find a way to uncomment these and make them work
//    data class TriggerInfo(
//        val columns: List<String> = TriggerInfoColumns.values().map {
//            it.name.toLowerCase(Locale.getDefault())
//        }
//    ) : PragmaParameters()
//
//    data class TableInfo(
//        val databasePath: String,
//        val database: SQLiteDatabase? = null,
//        val statement: String,
//        val order: Direction = Direction.ASCENDING,
//        val page: Int? = Constants.Limits.INITIAL_PAGE,
//        val pageSize: Int = Constants.Limits.PAGE_SIZE
//    ) : PragmaParameters()

    data class ForeignKeys(
        val databasePath: String,
        val database: SQLiteDatabase? = null,
        val statement: String,
        val order: Direction = Direction.ASCENDING,
        val page: Int? = Constants.Limits.INITIAL_PAGE,
        val pageSize: Int = Constants.Limits.PAGE_SIZE
    ) : PragmaParameters()

    data class Indexes(
        val databasePath: String,
        val database: SQLiteDatabase? = null,
        val statement: String,
        val order: Direction = Direction.ASCENDING,
        val page: Int? = Constants.Limits.INITIAL_PAGE,
        val pageSize: Int = Constants.Limits.PAGE_SIZE
    ) : PragmaParameters()
}