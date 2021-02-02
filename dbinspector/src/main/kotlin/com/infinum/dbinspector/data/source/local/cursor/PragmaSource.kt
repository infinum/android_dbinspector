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
import com.infinum.dbinspector.data.source.memory.pagination.Paginator
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

internal class PragmaSource(
    private val tableInfoPaginator: Paginator,
    private val foreignKeysPaginator: Paginator,
    private val indexesPaginator: Paginator,
    private val store: Sources.Local.Store
) : CursorSource(), Sources.Local.Pragma {

    override suspend fun getUserVersion(query: Query): QueryResult =
        store.current().let {
            suspendCancellableCoroutine { continuation ->
                if (query.database?.isOpen == true) {
                    runQuery(query)?.use { cursor ->
                        val result = when (cursor.moveToFirst()) {
                            true -> cursor.getString(0)
                            false -> ""
                        }

                        continuation.resume(
                            QueryResult(
                                rows = listOf(
                                    Row(
                                        position = 0,
                                        fields = listOf(
                                            Field(
                                                type = FieldType.STRING,
                                                text = result,
                                                linesCount = if (it.linesLimit) {
                                                    it.linesCount
                                                } else {
                                                    Int.MAX_VALUE
                                                },
                                                truncate = it.truncateMode,
                                                blobPreview = it.blobPreview
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

    override suspend fun getTableInfo(query: Query): QueryResult =
        store.current().let {
            suspendCancellableCoroutine { continuation ->
                collectRows(
                    query = query,
                    paginator = tableInfoPaginator,
                    settings = it,
                    continuation = continuation
                )
            }
        }

    override suspend fun getForeignKeys(query: Query): QueryResult =
        store.current().let {
            suspendCancellableCoroutine { continuation ->
                collectRows(
                    query = query,
                    paginator = foreignKeysPaginator,
                    settings = it,
                    continuation = continuation
                )
            }
        }

    override suspend fun getIndexes(query: Query): QueryResult =
        store.current().let {
            suspendCancellableCoroutine { continuation ->
                collectRows(
                    query = query,
                    paginator = indexesPaginator,
                    settings = it,
                    continuation = continuation
                )
            }
        }
}
