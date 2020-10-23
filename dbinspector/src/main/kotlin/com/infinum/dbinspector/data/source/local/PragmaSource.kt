package com.infinum.dbinspector.data.source.local

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.CursorException
import com.infinum.dbinspector.data.models.local.QueryException
import com.infinum.dbinspector.data.models.local.QueryResult
import com.infinum.dbinspector.data.models.local.Row
import com.infinum.dbinspector.data.source.local.shared.CursorSource
import com.infinum.dbinspector.data.source.memory.pagination.Paginator
import com.infinum.dbinspector.domain.shared.models.Query
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class PragmaSource(
    private val tableInfoPaginator: Paginator,
    private val foreignKeysPaginator: Paginator,
    private val indexesPaginator: Paginator
) : CursorSource(), Sources.Local.Pragma {

    override suspend fun getUserVersion(query: Query): QueryResult =
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
                                    fields = listOf(result)
                                )
                            )
                        )
                    )
                } ?: continuation.resumeWithException(CursorException())
            } else {
                continuation.resumeWithException(QueryException())
            }
        }

    override suspend fun getTableInfo(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = tableInfoPaginator,
                continuation = continuation
            )
        }

    override suspend fun getForeignKeys(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = foreignKeysPaginator,
                continuation = continuation
            )
        }

    override suspend fun getIndexes(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = indexesPaginator,
                continuation = continuation
            )
        }
}
