package im.dino.dbinspector.data.source.local

import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import im.dino.dbinspector.BuildConfig
import im.dino.dbinspector.data.Sources
import im.dino.dbinspector.data.models.local.FieldType
import im.dino.dbinspector.data.models.local.QueryException
import im.dino.dbinspector.data.models.local.QueryResult
import im.dino.dbinspector.data.models.local.Row
import im.dino.dbinspector.data.source.memory.Paginator
import im.dino.dbinspector.domain.shared.models.Query
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class PragmaSource(
    private val tableInfoPaginator: Paginator,
    private val foreignKeysPaginator: Paginator,
    private val indexesPaginator: Paginator
) : Sources.Local.Pragma {

    companion object {
        private const val COLUMN_BLOB_VALUE = "(data)"
    }

    override suspend fun getUserVersion(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            if (BuildConfig.DEBUG) {
                                println(it)
                            }
                        },
                    null
                ).use { cursor ->
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
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }

    override suspend fun getTableInfo(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            if (BuildConfig.DEBUG) {
                                println(it)
                            }
                        },
                    null
                ).use { cursor ->
                    tableInfoPaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = tableInfoPaginator.boundary(
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
                            nextPage = tableInfoPaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }

    override suspend fun getForeignKeys(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            if (BuildConfig.DEBUG) {
                                println(it)
                            }
                        },
                    null
                ).use { cursor ->
                    foreignKeysPaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = foreignKeysPaginator.boundary(
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
                            nextPage = foreignKeysPaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }

    override suspend fun getIndexes(query: Query): QueryResult =
        suspendCancellableCoroutine { continuation ->
            if (query.database?.isOpen == true) {
                query.database.rawQuery(
                    query.statement
                        .also {
                            if (BuildConfig.DEBUG) {
                                println(it)
                            }
                        },
                    null
                ).use { cursor ->
                    indexesPaginator.setPageCount(
                        cursor.count,
                        query.pageSize
                    )

                    val boundary = indexesPaginator.boundary(
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
                            nextPage = indexesPaginator.nextPage(query.page)
                        )
                    )
                }
            } else {
                continuation.resumeWithException(QueryException())
            }
        }
}
