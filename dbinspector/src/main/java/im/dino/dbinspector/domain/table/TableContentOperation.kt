package im.dino.dbinspector.domain.table

import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractDatabaseOperation

class TableContentOperation(
    private val name: String,
    private val pageSize: Int
) : AbstractDatabaseOperation<List<Row>>() {

    override fun query(): String = "\"$name\""

    override fun pageSize(): Int = pageSize

    override fun invoke(path: String, nextPage: Int?): List<Row> {
        database(path).use { database ->
            database.query(
                query(),
                null,
                null,
                null,
                null,
                null,
                null
            ).use { cursor ->
                pageCount(cursor.count)
                return collect(cursor, nextPage)
            }
        }
    }
}