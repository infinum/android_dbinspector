package im.dino.dbinspector.domain.database

import android.database.Cursor
import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractDatabaseOperation
import kotlin.math.min

class AllTablesOperation(
    private val pageSize: Int
) : AbstractDatabaseOperation<List<Row>>() {

    override fun query(): String = ALL_TABLES

    override fun pageSize(): Int = pageSize

    override fun invoke(path: String, nextPage: Int?): List<Row> {
        database(path).use { database ->
            database.rawQuery(
                query(),
                null
            ).use { cursor ->
                pageCount(cursor.count)
                return collect(cursor, nextPage)
            }
        }
    }

    override fun collect(cursor: Cursor, page: Int?): List<Row> {
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
                    fields.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)))
                }

                rows.add(
                    Row(
                        fields = fields
                    )
                )

                cursor.moveToNext()
            }
        }

        return rows
    }
}