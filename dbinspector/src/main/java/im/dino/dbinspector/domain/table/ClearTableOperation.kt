package im.dino.dbinspector.domain.table

import im.dino.dbinspector.domain.shared.AbstractDatabaseOperation

class ClearTableOperation(
    private val name: String,
) : AbstractDatabaseOperation<Boolean>() {

    override fun query(): String = ""

    override fun pageSize(): Int = 0

    override fun invoke(path: String, nextPage: Int?): Boolean {
        return database(path).use { database ->
            database.delete(
                "\"$name\"",
                null,
                null
            ) > 0
        }
    }
}