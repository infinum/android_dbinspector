package com.infinum.dbinspector.data.source.local.shared

import android.database.Cursor
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.infinum.dbinspector.data.models.local.CursorException
import com.infinum.dbinspector.data.models.local.FieldType
import com.infinum.dbinspector.data.models.local.QueryException
import com.infinum.dbinspector.data.models.local.QueryResult
import com.infinum.dbinspector.data.models.local.Row
import com.infinum.dbinspector.data.source.memory.pagination.Paginator
import com.infinum.dbinspector.domain.shared.models.Query
import kotlinx.coroutines.CancellableContinuation
import timber.log.Timber
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal open class CursorSource {

    companion object {
        private const val COLUMN_BLOB_VALUE = "(data)"
    }

    fun collectRows(query: Query, paginator: Paginator, continuation: CancellableContinuation<QueryResult>) {
        if (query.database?.isOpen == true) {
            runQuery(query)?.use { cursor ->
                paginator.setPageCount(
                    cursor.count,
                    query.pageSize
                )

                val boundary = paginator.boundary(
                    query.page,
                    query.pageSize,
                    cursor.count
                )

                val count = paginator.count(
                    boundary.startRow,
                    boundary.endRow,
                    cursor.count,
                    cursor.columnCount
                )

                val rows = iterateRowsInTable(cursor, boundary)

                continuation.resume(
                    QueryResult(
                        rows = rows,
                        nextPage = paginator.nextPage(query.page),
                        beforeCount = count.beforeCount,
                        afterCount = count.afterCount
                    )
                )
            } ?: continuation.resumeWithException(CursorException())
        } else {
            continuation.resumeWithException(QueryException())
        }
    }

    internal fun runQuery(query: Query): Cursor? =
        query.database?.rawQuery(
            query.statement.also { Timber.i(it) },
            null
        )

    private fun iterateRowsInTable(cursor: Cursor, boundary: Paginator.Boundary): List<Row> =
        if (cursor.moveToPosition(boundary.startRow)) {
            (boundary.startRow until boundary.endRow).map { row ->
                Row(
                    position = row,
                    fields = iterateFieldsInRow(cursor)
                ).also {
                    cursor.moveToNext()
                }
            }
        } else {
            listOf()
        }

    private fun iterateFieldsInRow(cursor: Cursor): List<String> =
        (0 until cursor.columnCount).map { column ->
            when (FieldType(cursor.getType(column))) {
                FieldType.NULL -> FieldType.NULL.name.toLowerCase(Locale.getDefault())
                FieldType.INTEGER -> cursor.getIntOrNull(column)?.toString()
                    ?: FieldType.NULL.name.toLowerCase(Locale.getDefault())
                FieldType.FLOAT -> cursor.getFloatOrNull(column)?.toString()
                    ?: FieldType.NULL.name.toLowerCase(Locale.getDefault())
                FieldType.STRING -> cursor.getStringOrNull(column)
                    ?: FieldType.NULL.name.toLowerCase(Locale.getDefault())
                FieldType.BLOB -> COLUMN_BLOB_VALUE
            }
        }
}
