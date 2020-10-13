package im.dino.dbinspector.domain.shared.models

import android.database.sqlite.SQLiteDatabase
import im.dino.dbinspector.ui.shared.Constants

data class Query(
    val databasePath: String = "",
    val database: SQLiteDatabase? = null,
    val statement: String,
    val sort: Sort = Sort.ASCENDING,
    val pageSize: Int = Constants.Limits.PAGE_SIZE,
    val page: Int? = Constants.Limits.INITIAL_PAGE
)