package com.infinum.dbinspector.data.source.local.cursor.shared

import android.database.Cursor
import androidx.core.database.getBlobOrNull
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.infinum.dbinspector.data.models.local.cursor.exceptions.CursorException
import com.infinum.dbinspector.data.models.local.cursor.exceptions.QueryException
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.cursor.output.Row
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.data.source.memory.pagination.Paginator
import com.infinum.dbinspector.extensions.lowercase
import kotlinx.coroutines.CancellableContinuation
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal open class CursorSource {

    fun collectRows(
        query: Query,
        paginator: Paginator,
        settings: SettingsEntity?,
        filterPredicate: ((Field) -> Boolean) = { false },
        continuation: CancellableContinuation<QueryResult>
    ) {
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

                val rows = iterateRowsInTable(cursor, boundary, settings, filterPredicate)

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
        settings: SettingsEntity?,
        filterPredicate: (Field) -> Boolean
    ): List<Row> =
        if (cursor.moveToPosition(boundary.startRow)) {
            (boundary.startRow until boundary.endRow).map { row ->
                Row(
                    position = row,
                    fields = iterateFieldsInRow(
                        cursor,
                        settings ?: SettingsEntity.getDefaultInstance(),
                        filterPredicate
                    )
                ).also {
                    cursor.moveToNext()
                }
            }
        } else {
            listOf()
        }

    private fun iterateFieldsInRow(
        cursor: Cursor,
        settings: SettingsEntity,
        filterPredicate: (Field) -> Boolean
    ): List<Field> =
        (0 until cursor.columnCount).map { column ->
            when (val type = FieldType(cursor.getType(column))) {
                FieldType.NULL -> Field(
                    type = type,
                    text = FieldType.NULL.name.lowercase(),
                    linesCount = if (settings.linesLimit) settings.linesCount else Int.MAX_VALUE,
                    truncate = settings.truncateMode,
                    blobPreview = settings.blobPreview
                )
                FieldType.INTEGER -> Field(
                    type = type,
                    text = cursor.getIntOrNull(column)?.toString()
                        ?: FieldType.NULL.name.lowercase(),
                    linesCount = if (settings.linesLimit) settings.linesCount else Int.MAX_VALUE,
                    truncate = settings.truncateMode,
                    blobPreview = settings.blobPreview
                )
                FieldType.FLOAT -> Field(
                    type = type,
                    text = cursor.getFloatOrNull(column)?.toString()
                        ?: FieldType.NULL.name.lowercase(),
                    linesCount = if (settings.linesLimit) settings.linesCount else Int.MAX_VALUE,
                    truncate = settings.truncateMode,
                    blobPreview = settings.blobPreview
                )
                FieldType.STRING -> Field(
                    type = type,
                    text = cursor.getStringOrNull(column)
                        ?: FieldType.NULL.name.lowercase(),
                    linesCount = if (settings.linesLimit) settings.linesCount else Int.MAX_VALUE,
                    truncate = settings.truncateMode,
                    blobPreview = settings.blobPreview
                )
                FieldType.BLOB -> Field(
                    type = type,
                    text = FieldType.NULL.name.lowercase(),
                    data = cursor.getBlobOrNull(column),
                    linesCount = if (settings.linesLimit) settings.linesCount else Int.MAX_VALUE,
                    truncate = settings.truncateMode,
                    blobPreview = settings.blobPreview
                )
            }
        }
            .filterNot(filterPredicate)
}
