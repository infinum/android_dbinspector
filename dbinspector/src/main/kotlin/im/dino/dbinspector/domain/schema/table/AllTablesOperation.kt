package im.dino.dbinspector.domain.schema.table

import android.database.Cursor
import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractDatabaseOperation
import kotlin.math.min

internal class AllTablesOperation(
    private val pageSize: Int,
    private val args: String?
) : AbstractDatabaseOperation<List<Row>>() {

    override fun query(): String = if (args.isNullOrBlank()) {
        String.format(FORMAT_ALL_TABLES, "ASC")
    } else {
        String.format(FORMAT_SEARCH_TABLES, args, "ASC")
    }

    override fun pageSize(): Int = pageSize

    override fun invoke(path: String, nextPage: Int?): List<Row> {
        database(path).use { database ->
            database.rawQuery(
                query(),
                null
            ).use { cursor ->
                pageCount(cursor.count, cursor.columnCount)
                return collect(cursor, nextPage)
            }
        }
    }

    override fun collect(cursor: Cursor, page: Int?): List<Row> {
        val startRow = page?.let {
            pageSize() * it
        } ?: 0
        val endRow = min(startRow + pageSize(), cursor.count)

        return if (cursor.moveToPosition(startRow)) {
            (startRow until endRow).map { row ->
                Row(
                    position = row,
                    fields = (0 until cursor.columnCount).map {
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    }
                ).also {
                    cursor.moveToNext()
                }
            }
        } else {
            listOf()
        }
    }
}