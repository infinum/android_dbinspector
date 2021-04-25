package com.infinum.dbinspector.data.source.local.cursor

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.exceptions.CursorException
import com.infinum.dbinspector.data.models.local.cursor.exceptions.QueryException
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.cursor.output.Row
import com.infinum.dbinspector.data.source.local.cursor.shared.CursorSource
import com.infinum.dbinspector.data.source.memory.logger.Logger
import com.infinum.dbinspector.data.source.memory.pagination.Paginator
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

internal class RawQuerySource(
    private val rawQueryPaginator: Paginator,
    private val store: Sources.Local.Settings,
    logger: Logger
) : CursorSource(logger), Sources.Local.RawQuery {

    override suspend fun rawQueryHeaders(query: Query): QueryResult =
        store.current().let {
            suspendCancellableCoroutine { continuation ->
                if (query.database?.isOpen == true) {
                    runQuery(query)?.use { cursor ->
                        cursor.moveToFirst()

                        continuation.resume(
                            QueryResult(
                                rows = listOf(
                                    Row(
                                        position = 0,
                                        fields = cursor.columnNames.toList().map { name ->
                                            Field(
                                                type = FieldType.STRING,
                                                text = name,
                                                linesCount = if (it.linesLimit) {
                                                    it.linesCount
                                                } else {
                                                    Int.MAX_VALUE
                                                },
                                                truncate = it.truncateMode,
                                                blobPreview = it.blobPreview
                                            )
                                        }
                                    )
                                )
                            )
                        )
                    } ?: continuation.resumeWithException(CursorException())
                } else {
                    continuation.resumeWithException(QueryException())
                }
            }
        }

    override suspend fun rawQuery(query: Query): QueryResult =
        store.current().let {
            suspendCancellableCoroutine { continuation ->
                collectRows(
                    query = query,
                    paginator = rawQueryPaginator,
                    settings = it,
                    continuation = continuation
                )
            }
        }

    override suspend fun affectedRows(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                runQuery(query)?.use { cursor ->
                    val result = when (cursor.moveToFirst()) {
                        true -> cursor.getString(0)
                        false -> "0"
                    }

                    continuation.resume(
                        QueryResult(
                            rows = listOf(
                                Row(
                                    position = 0,
                                    fields = listOf(
                                        Field(
                                            type = FieldType.STRING,
                                            text = result
                                        )
                                    )
                                )
                            )
                        )
                    )
                } ?: continuation.resumeWithException(CursorException())
            } else {
                continuation.resumeWithException(QueryException())
            }
        }
}
