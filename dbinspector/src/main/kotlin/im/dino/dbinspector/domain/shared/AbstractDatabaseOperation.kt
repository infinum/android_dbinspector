package im.dino.dbinspector.domain.shared

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.models.FieldType
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

internal abstract class AbstractDatabaseOperation<T> : DatabaseOperation<T> {

    companion object {
        internal const val FORMAT_ALL_TABLES = "SELECT name FROM sqlite_master WHERE type = 'table' ORDER BY name %s"
        internal const val FORMAT_ALL_VIEWS = "SELECT name FROM sqlite_master WHERE type = 'view' ORDER BY name %s"
        internal const val FORMAT_ALL_TRIGGERS = "SELECT name, sql FROM sqlite_master WHERE type='trigger' ORDER BY name %s"

        internal const val FORMAT_SEARCH_TABLES = "SELECT name FROM sqlite_master WHERE type='table' AND name LIKE \"%%%s%%\" ORDER BY name %s"
        internal const val FORMAT_SEARCH_VIEWS = "SELECT name FROM sqlite_master WHERE type = 'view' AND name LIKE \"%%%s%%\" ORDER BY name %s"
        internal const val FORMAT_SEARCH_TRIGGERS = "SELECT name, sql FROM sqlite_master WHERE type = 'trigger' AND name LIKE \"%%%s%%\" ORDER BY name %s"

        internal const val FORMAT_PRAGMA_TABLE_INFO = "PRAGMA table_info(\"%s\")"
        internal const val FORMAT_PRAGMA_FOREIGN_KEYS = "PRAGMA foreign_key_list(\"%s\")"
        internal const val FORMAT_PRAGMA_INDEXES = "PRAGMA index_list(\"%s\")"

        internal const val PRAGMA_USER_VERSION = "PRAGMA user_version"
        internal const val FORMAT_PRAGMA_USER_VERSION = "PRAGMA user_version = '%s'"
        internal const val PRAGMA_JOURNAL_MODE = "PRAGMA journal_mode"
        internal const val FORMAT_PRAGMA_JOURNAL_MODE = "PRAGMA journal_mode = '%s'"

        internal const val FORMAT_CONTENT_VIEW = "SELECT * FROM \"%s\""
        internal const val FORMAT_DROP_VIEW = "DROP VIEW \"%s\""

        internal const val FORMAT_CONTENT_TRIGGER = "SELECT name, sql FROM sqlite_master WHERE type = 'trigger' AND name = \"%s\" LIMIT 1"
        internal const val FORMAT_DROP_TRIGGER = "DROP TRIGGER \"%s\""

        internal const val COLUMN_NAME = "name"
        internal const val COLUMN_BLOB_VALUE = "(data)"
    }

    private var pageCount: Int = 1

    private var currentPage: Int = 1

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
        val startRow = page?.let {
            pageSize() * it
        } ?: 0
        val endRow = min(startRow + pageSize(), cursor.count)

        return if (cursor.moveToPosition(startRow)) {
            (startRow until endRow).map { row ->
                Row(
                    position = row,
                    fields = (0 until cursor.columnCount).map { column ->
                        when (FieldType(cursor.getType(column))) {
                            FieldType.NULL -> FieldType.NULL.name.toLowerCase(Locale.getDefault())
                            FieldType.INTEGER -> cursor.getIntOrNull(column)?.toString()
                                ?: FieldType.NULL.name.toLowerCase(Locale.getDefault())
                            FieldType.FLOAT -> cursor.getFloatOrNull(column)?.toString()
                                ?: FieldType.NULL.name.toLowerCase(Locale.getDefault())
                            FieldType.STRING -> cursor.getStringOrNull(column) ?: FieldType.NULL.name.toLowerCase(Locale.getDefault())
                            FieldType.BLOB -> COLUMN_BLOB_VALUE
                        }
                    }
                ).also {
                    cursor.moveToNext()
                }
            }
        } else {
            listOf()
        } as T
    }
}