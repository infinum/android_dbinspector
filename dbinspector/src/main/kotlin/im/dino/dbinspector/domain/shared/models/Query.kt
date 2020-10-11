package im.dino.dbinspector.domain.shared.models

import android.database.sqlite.SQLiteDatabase

data class Query(
    val databasePath: String = "",
    val database: SQLiteDatabase? = null,
    val statement: String,
    val sort: Sort = Sort.ASCENDING,
    val pageSize: Int = 1,
    val page: Int? = 1
)