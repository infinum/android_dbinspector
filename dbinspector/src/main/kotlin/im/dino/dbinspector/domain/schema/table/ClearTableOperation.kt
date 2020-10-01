package im.dino.dbinspector.domain.schema.table

import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractDatabaseOperation

internal class ClearTableOperation(
    private val name: String,
) : AbstractDatabaseOperation<List<Row>>() {

    override fun query(): String = ""

    override fun pageSize(): Int = 0

    override fun invoke(path: String, nextPage: Int?): List<Row> {
        return database(path).use { database ->
            val result = database.delete(
                "\"$name\"",
                null,
                null
            ) > 0
            if (result) {
                listOf()
            } else {
                listOf(
                    Row(
                        0,
                        listOf()
                    )
                )
            }
        }
    }
}