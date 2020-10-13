package im.dino.dbinspector.data.source.local

import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import im.dino.dbinspector.data.Sources
import im.dino.dbinspector.data.models.local.FieldType
import im.dino.dbinspector.data.models.local.QueryException
import im.dino.dbinspector.data.models.local.QueryResult
import im.dino.dbinspector.data.models.local.Row
import im.dino.dbinspector.data.source.memory.Paginator
import im.dino.dbinspector.domain.shared.models.Query
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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
) : Sources.Local.Schema {

    companion object {
        private const val COLUMN_BLOB_VALUE = "(data)"
    }

    // region Tables
    override suspend fun getTables(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            Timber.i(it)
                        },
                    null
                ).use { cursor ->
                    tablesPaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = tablesPaginator.boundary(
                        query.page,
                        query.pageSize,
                        cursor.count
                    )

                    val rows = if (cursor.moveToPosition(boundary.startRow)) {
                        (boundary.startRow until boundary.endRow).map { row ->
                            Row(
                                position = row,
                                fields = (0 until cursor.columnCount).map { column ->
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
                            ).also {
                                cursor.moveToNext()
                            }
                        }
                    } else {
                        listOf()
                    }

                    continuation.resume(
                        QueryResult(
                            rows = rows,
                            nextPage = tablesPaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }

    override suspend fun getTableByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            Timber.i(it)
                        },
                    null
                ).use { cursor ->
                    tableByNamePaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = tableByNamePaginator.boundary(
                        query.page,
                        query.pageSize,
                        cursor.count
                    )

                    val rows = if (cursor.moveToPosition(boundary.startRow)) {
                        (boundary.startRow until boundary.endRow).map { row ->
                            Row(
                                position = row,
                                fields = (0 until cursor.columnCount).map { column ->
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
                            ).also {
                                cursor.moveToNext()
                            }
                        }
                    } else {
                        listOf()
                    }

                    continuation.resume(
                        QueryResult(
                            rows = rows,
                            nextPage = tableByNamePaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }

    override suspend fun dropTableContentByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            Timber.i(it)
                        },
                    null
                ).use { cursor ->
                    dropTableContentPaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = dropTableContentPaginator.boundary(
                        query.page,
                        query.pageSize,
                        cursor.count
                    )

                    val rows = if (cursor.moveToPosition(boundary.startRow)) {
                        (boundary.startRow until boundary.endRow).map { row ->
                            Row(
                                position = row,
                                fields = (0 until cursor.columnCount).map { column ->
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
                            ).also {
                                cursor.moveToNext()
                            }
                        }
                    } else {
                        listOf()
                    }

                    continuation.resume(
                        QueryResult(
                            rows = rows,
                            nextPage = dropTableContentPaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }
    // endregion

    // region Views
    override suspend fun getViews(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            Timber.i(it)
                        },
                    null
                ).use { cursor ->
                    viewsPaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = viewsPaginator.boundary(
                        query.page,
                        query.pageSize,
                        cursor.count
                    )

                    val rows = if (cursor.moveToPosition(boundary.startRow)) {
                        (boundary.startRow until boundary.endRow).map { row ->
                            Row(
                                position = row,
                                fields = (0 until cursor.columnCount).map { column ->
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
                            ).also {
                                cursor.moveToNext()
                            }
                        }
                    } else {
                        listOf()
                    }

                    continuation.resume(
                        QueryResult(
                            rows = rows,
                            nextPage = viewsPaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }

    override suspend fun getViewByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            Timber.i(it)
                        },
                    null
                ).use { cursor ->
                    viewByNamePaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = viewByNamePaginator.boundary(
                        query.page,
                        query.pageSize,
                        cursor.count
                    )

                    val rows = if (cursor.moveToPosition(boundary.startRow)) {
                        (boundary.startRow until boundary.endRow).map { row ->
                            Row(
                                position = row,
                                fields = (0 until cursor.columnCount).map { column ->
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
                            ).also {
                                cursor.moveToNext()
                            }
                        }
                    } else {
                        listOf()
                    }

                    continuation.resume(
                        QueryResult(
                            rows = rows,
                            nextPage = viewByNamePaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }

    override suspend fun dropViewByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            Timber.i(it)
                        },
                    null
                ).use { cursor ->
                    dropViewPaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = dropViewPaginator.boundary(
                        query.page,
                        query.pageSize,
                        cursor.count
                    )

                    val rows = if (cursor.moveToPosition(boundary.startRow)) {
                        (boundary.startRow until boundary.endRow).map { row ->
                            Row(
                                position = row,
                                fields = (0 until cursor.columnCount).map { column ->
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
                            ).also {
                                cursor.moveToNext()
                            }
                        }
                    } else {
                        listOf()
                    }

                    continuation.resume(
                        QueryResult(
                            rows = rows,
                            nextPage = dropViewPaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }
    // endregion

    // region Triggers
    override suspend fun getTriggers(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            Timber.i(it)
                        },
                    null
                ).use { cursor ->
                    triggersPaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = triggersPaginator.boundary(
                        query.page,
                        query.pageSize,
                        cursor.count
                    )

                    val rows = if (cursor.moveToPosition(boundary.startRow)) {
                        (boundary.startRow until boundary.endRow).map { row ->
                            Row(
                                position = row,
                                fields = (0 until cursor.columnCount).map { column ->
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
                            ).also {
                                cursor.moveToNext()
                            }
                        }
                    } else {
                        listOf()
                    }

                    continuation.resume(
                        QueryResult(
                            rows = rows,
                            nextPage = triggersPaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }

    override suspend fun getTriggerByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            Timber.i(it)
                        },
                    null
                ).use { cursor ->
                    triggerByNamePaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = triggerByNamePaginator.boundary(
                        query.page,
                        query.pageSize,
                        cursor.count
                    )

                    val rows = if (cursor.moveToPosition(boundary.startRow)) {
                        (boundary.startRow until boundary.endRow).map { row ->
                            Row(
                                position = row,
                                fields = (0 until cursor.columnCount).map { column ->
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
                            ).also {
                                cursor.moveToNext()
                            }
                        }
                    } else {
                        listOf()
                    }

                    continuation.resume(
                        QueryResult(
                            rows = rows,
                            nextPage = triggerByNamePaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }

    override suspend fun dropTriggerByName(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            Timber.i(it)
                        },
                    null
                ).use { cursor ->
                    dropTriggerPaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = dropTriggerPaginator.boundary(
                        query.page,
                        query.pageSize,
                        cursor.count
                    )

                    val rows = if (cursor.moveToPosition(boundary.startRow)) {
                        (boundary.startRow until boundary.endRow).map { row ->
                            Row(
                                position = row,
                                fields = (0 until cursor.columnCount).map { column ->
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
                            ).also {
                                cursor.moveToNext()
                            }
                        }
                    } else {
                        listOf()
                    }

                    continuation.resume(
                        QueryResult(
                            rows = rows,
                            nextPage = dropTriggerPaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }
    // endregion
}
