package im.dino.dbinspector.domain.shared

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

internal abstract class AbstractPragmaOperation : PragmaOperation<String> {

    internal fun database(path: String): SQLiteDatabase =
        SQLiteDatabase.openOrCreateDatabase(path, null)

    override fun invoke(path: String, value: String?): String {
        database(path).use { database ->
            database.rawQuery(
                value?.let { String.format(query(), it) } ?: query(),
                null
            ).use { cursor ->
                return collect(cursor)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun collect(cursor: Cursor): String =
        when (cursor.moveToFirst()) {
            true -> cursor.getString(0)
            false -> ""
        }
}
