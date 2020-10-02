package im.dino.dbinspector.domain.shared

import im.dino.dbinspector.ui.shared.base.BaseViewModel.Companion.PAGE_SIZE

internal abstract class AbstractSchemaOperation<T>(
) : AbstractDatabaseOperation<T>() {

    override fun pageSize(): Int = PAGE_SIZE

    override fun invoke(path: String, nextPage: Int?): T {
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
}