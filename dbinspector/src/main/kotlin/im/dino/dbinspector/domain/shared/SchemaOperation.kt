package im.dino.dbinspector.domain.shared

import android.database.Cursor

internal interface SchemaOperation<T> {

    fun query(): String

    operator fun invoke(path: String, value: String?): T

    fun collect(cursor: Cursor): T
}
