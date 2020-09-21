package im.dino.dbinspector.domain.shared

import android.database.Cursor

interface DatabaseOperation<T> {

    operator fun invoke(path: String, nextPage: Int?): T

    fun collect(cursor: Cursor, page: Int?): T
}