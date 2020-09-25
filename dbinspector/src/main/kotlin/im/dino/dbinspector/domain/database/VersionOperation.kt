package im.dino.dbinspector.domain.database

import android.database.Cursor
import im.dino.dbinspector.domain.shared.AbstractDatabaseOperation

internal class VersionOperation : AbstractDatabaseOperation<String>() {

    override fun query(): String = DATABASE_VERSION

    override fun pageSize(): Int = 1

    override fun invoke(path: String, nextPage: Int?): String {
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

    override fun collect(cursor: Cursor, page: Int?): String =
        when (cursor.moveToFirst()) {
            true -> cursor.getString(0)
            false -> ""
        }
}