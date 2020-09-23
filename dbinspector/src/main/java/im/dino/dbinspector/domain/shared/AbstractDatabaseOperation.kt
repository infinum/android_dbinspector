package im.dino.dbinspector.domain.shared

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.pragma.models.FieldType
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

abstract class AbstractDatabaseOperation<T> : DatabaseOperation<T> {

    companion object {
        internal const val DATABASE_VERSION = "PRAGMA user_version"
        internal const val ALL_TABLES = "SELECT name FROM sqlite_master WHERE type='table' ORDER by name asc"

        internal const val FORMAT_ALL_TABLES = "SELECT name FROM sqlite_master WHERE type='table' AND name LIKE \"%%%s%%\" ORDER by name asc"
        internal const val FORMAT_TABLE_INFO = "PRAGMA table_info(\"%s\")"
        internal const val FORMAT_FOREIGN_KEYS = "PRAGMA foreign_key_list(\"%s\")"
        internal const val FORMAT_INDEXES = "PRAGMA index_list(\"%s\")"

        internal const val COLUMN_NAME = "name"
        internal const val COLUMN_BLOB_VALUE = "(data)"
    }

    private var pageCount: Int = 1

    private var currentPage: Int = 1

    abstract fun query(): String

    abstract fun pageSize(): Int

    internal fun database(path: String): SQLiteDatabase =
        SQLiteDatabase.openOrCreateDatabase(path, null)

    internal fun pageCount(rowCount: Int) {
        pageCount = ceil(rowCount.toDouble() / pageSize()).roundToInt()
    }

    fun nextPage(): Int? =
        when (currentPage == pageCount) {
            true -> null
            false -> {
                currentPage = currentPage.inc()
                currentPage
            }
        }

    @Suppress("UNCHECKED_CAST")
    override fun collect(cursor: Cursor, page: Int?): T {
        val rowCount = cursor.count
        val columnCount = cursor.columnCount

        val startRow = page?.let {
            pageSize() * it
        } ?: 0
        val endRow = min(startRow + pageSize(), rowCount)

        val rows: MutableList<Row> = mutableListOf()
        if (cursor.moveToPosition(startRow)) {
            for (row in startRow until endRow) {

                val fields: MutableList<String> = mutableListOf()
                for (column in 0 until columnCount) {
                    fields.add(
                        when (FieldType(cursor.getType(column)) == FieldType.BLOB) {
                            true -> COLUMN_BLOB_VALUE
                            false -> cursor.getString(column) ?: "null"
                        }
                    )
                }

                rows.add(
                    Row(
                        fields = fields
                    )
                )

                cursor.moveToNext()
            }
        }

        return rows as T
    }
}