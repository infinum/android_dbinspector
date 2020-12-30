package com.infinum.dbinspector.data.source.local.cursor

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.exceptions.CursorException
import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.cursor.exceptions.QueryException
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.cursor.output.Row
import com.infinum.dbinspector.data.source.local.cursor.shared.CursorSource
import com.infinum.dbinspector.data.source.memory.pagination.Paginator
import com.infinum.dbinspector.data.models.local.cursor.input.Query
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
                                    fields = listOf(Field(type = FieldType.STRING, text = result))
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
