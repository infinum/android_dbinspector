package com.infinum.dbinspector.data.source.local

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.QueryResult
import com.infinum.dbinspector.data.source.local.shared.CursorSource
import com.infinum.dbinspector.data.source.memory.pagination.Paginator
import com.infinum.dbinspector.domain.shared.models.Query
import kotlinx.coroutines.suspendCancellableCoroutine

@Suppress("LongParameterList")
internal class SchemaSource(
    private val tablesPaginator: Paginator,
    private val tableByNamePaginator: Paginator,
    private val dropTableContentPaginator: Paginator,
    private val viewsPaginator: Paginator,
    private val viewByNamePaginator: Paginator,
    private val dropViewPaginator: Paginator,
    private val triggersPaginator: Paginator,
    private val triggerByNamePaginator: Paginator,
    private val dropTriggerPaginator: Paginator
) : CursorSource(), Sources.Local.Schema {

    // region Tables
    override suspend fun getTables(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = tablesPaginator,
                continuation = continuation
            )
        }

    override suspend fun getTableByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = tableByNamePaginator,
                continuation = continuation
            )
        }

    override suspend fun dropTableContentByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = dropTableContentPaginator,
                continuation = continuation
            )
        }
    // endregion

    // region Views
    override suspend fun getViews(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = viewsPaginator,
                continuation = continuation
            )
        }

    override suspend fun getViewByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = viewByNamePaginator,
                continuation = continuation
            )
        }

    override suspend fun dropViewByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = dropViewPaginator,
                continuation = continuation
            )
        }
    // endregion

    // region Triggers
    override suspend fun getTriggers(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = triggersPaginator,
                continuation = continuation
            )
        }

    override suspend fun getTriggerByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = triggerByNamePaginator,
                continuation = continuation
            )
        }

    override suspend fun dropTriggerByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            collectRows(
                query = query,
                paginator = dropTriggerPaginator,
                continuation = continuation
            )
        }
    // endregion
}
