package im.dino.dbinspector.domain.shared

import android.database.Cursor

interface DatabaseOperation<T> {

    fun query(): String

    fun pageSize(): Int

    operator fun invoke(path: String, nextPage: Int?): T

    fun collect(cursor: Cursor, page: Int?): T
}