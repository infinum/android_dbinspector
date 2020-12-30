package com.infinum.dbinspector.data.source.local.cursor.shared

import android.database.Cursor
import androidx.core.database.getBlobOrNull
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.infinum.dbinspector.data.models.local.cursor.exceptions.CursorException
import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.cursor.exceptions.QueryException
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.cursor.output.Row
import com.infinum.dbinspector.data.source.memory.pagination.Paginator
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import kotlinx.coroutines.CancellableContinuation
import timber.log.Timber
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal open class CursorSource {

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

                val rows = iterateRowsInTable(cursor, boundary, query.blobPreview)

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

    private fun iterateRowsInTable(
        cursor: Cursor,
        boundary: Paginator.Boundary,
        blobPreview: SettingsEntity.BlobPreviewMode
    ): List<Row> =
        if (cursor.moveToPosition(boundary.startRow)) {
            (boundary.startRow until boundary.endRow).map { row ->
                Row(
                    position = row,
                    fields = iterateFieldsInRow(cursor, blobPreview)
                ).also {
                    cursor.moveToNext()
                }
            }
        } else {
            listOf()
        }

    private fun iterateFieldsInRow(cursor: Cursor, blobPreview: SettingsEntity.BlobPreviewMode): List<Field> =
        (0 until cursor.columnCount).map { column ->
            when (val type = FieldType(cursor.getType(column))) {
                FieldType.NULL -> Field(
                    type = type,
                    text = FieldType.NULL.name.toLowerCase(Locale.getDefault())
                )
                FieldType.INTEGER -> Field(
                    type = type,
                    text = cursor.getIntOrNull(column)?.toString()
                        ?: FieldType.NULL.name.toLowerCase(Locale.getDefault())
                )
                FieldType.FLOAT -> Field(
                    type = type,
                    text = cursor.getFloatOrNull(column)?.toString()
                        ?: FieldType.NULL.name.toLowerCase(Locale.getDefault())
                )
                FieldType.STRING -> Field(
                    type = type,
                    text = cursor.getStringOrNull(column)
                        ?: FieldType.NULL.name.toLowerCase(Locale.getDefault())
                )
                FieldType.BLOB -> Field(
                    type = type,
                    text = FieldType.NULL.name.toLowerCase(Locale.getDefault()),
                    data = cursor.getBlobOrNull(column),
                    blobPreview = blobPreview
                )
            }
        }
}
