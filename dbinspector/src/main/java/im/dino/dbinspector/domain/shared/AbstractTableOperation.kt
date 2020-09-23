package im.dino.dbinspector.domain.shared

abstract class AbstractTableOperation<T>(
    private val pageSize: Int
) : AbstractDatabaseOperation<T>() {

    override fun pageSize(): Int = pageSize

    override fun invoke(path: String, nextPage: Int?): T {
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
}