package com.infinum.dbinspector.server.controllers

import android.content.Context
import com.infinum.dbinspector.data.models.remote.PageResponse
import com.infinum.dbinspector.data.models.remote.RowResponse
import com.infinum.dbinspector.di.LibraryKoin
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.pragma.models.ForeignKeyListColumns
import com.infinum.dbinspector.domain.pragma.models.IndexListColumns
import com.infinum.dbinspector.domain.pragma.models.TableInfoColumns
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.extensions.lowercase
import com.infinum.dbinspector.extensions.toSha1
import io.ktor.utils.io.core.toByteArray
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class PragmaController(
    private val context: Context
) : KoinComponent {

    private val getDatabases: UseCases.GetDatabases by inject()

    private val getTables: UseCases.GetTables by inject()
    private val getViews: UseCases.GetViews by inject()

    private val getTableInfo: UseCases.GetTablePragma by inject()
    private val getForeignKeys: UseCases.GetForeignKeys by inject()
    private val getIndexes: UseCases.GetIndexes by inject()

    override fun getKoin(): Koin = LibraryKoin.koin()

    suspend fun getTableInfo(
        databaseId: String,
        id: String
    ): PageResponse? =
        getDatabases(
            DatabaseParameters.Get(
                context = context, argument = null
            )
        )
            .find { databaseId == it.absolutePath.toByteArray().toSha1() }
            ?.let { database ->
                getTables(
                    ContentParameters(
                        databasePath = database.absolutePath,
                        statement = Statements.Schema.tables(query = null)
                    )
                )
                    .cells
                    .find { id == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> =
                            TableInfoColumns.values().map { it.name.lowercase() }

                        val page = getTableInfo(
                            PragmaParameters.Pragma(
                                databasePath = database.absolutePath,
                                statement = Statements.Pragma.tableInfo(name)
                            )
                        )

                        PageResponse(
                            nextPage = page.nextPage,
                            beforeCount = page.beforeCount,
                            afterCount = page.afterCount,
                            columns = headers,
                            rows = page.cells.chunked(headers.size) {
                                RowResponse(
                                    cells = it.map { cell -> cell.text.orEmpty() }
                                )
                            }
                        )
                    }
            }

    suspend fun getTableForeignKeys(
        databaseId: String,
        id: String
    ): PageResponse? =
        getDatabases(
            DatabaseParameters.Get(
                context = context, argument = null
            )
        )
            .find { databaseId == it.absolutePath.toByteArray().toSha1() }
            ?.let { database ->
                getTables(
                    ContentParameters(
                        databasePath = database.absolutePath,
                        statement = Statements.Schema.tables(query = null)
                    )
                )
                    .cells
                    .find { id == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> =
                            ForeignKeyListColumns.values().map { it.name.lowercase() }

                        val page = getForeignKeys(
                            PragmaParameters.Pragma(
                                databasePath = database.absolutePath,
                                statement = Statements.Pragma.foreignKeys(name)
                            )
                        )

                        PageResponse(
                            nextPage = page.nextPage,
                            beforeCount = page.beforeCount,
                            afterCount = page.afterCount,
                            columns = headers,
                            rows = page.cells.chunked(headers.size) {
                                RowResponse(
                                    cells = it.map { cell -> cell.text.orEmpty() }
                                )
                            }
                        )
                    }
            }

    suspend fun getTableIndexes(
        databaseId: String,
        id: String
    ): PageResponse? =
        getDatabases(
            DatabaseParameters.Get(
                context = context, argument = null
            )
        )
            .find { databaseId == it.absolutePath.toByteArray().toSha1() }
            ?.let { database ->
                getTables(
                    ContentParameters(
                        databasePath = database.absolutePath,
                        statement = Statements.Schema.tables(query = null)
                    )
                )
                    .cells
                    .find { id == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> =
                            IndexListColumns.values().map { it.name.lowercase() }

                        val page = getIndexes(
                            PragmaParameters.Pragma(
                                databasePath = database.absolutePath,
                                statement = Statements.Pragma.indexes(name)
                            )
                        )

                        PageResponse(
                            nextPage = page.nextPage,
                            beforeCount = page.beforeCount,
                            afterCount = page.afterCount,
                            columns = headers,
                            rows = page.cells.chunked(headers.size) {
                                RowResponse(
                                    cells = it.map { cell -> cell.text.orEmpty() }
                                )
                            }
                        )
                    }
            }

    suspend fun getViewInfo(
        databaseId: String,
        id: String
    ): PageResponse? =
        getDatabases(
            DatabaseParameters.Get(
                context = context, argument = null
            )
        )
            .find { databaseId == it.absolutePath.toByteArray().toSha1() }
            ?.let { database ->
                getViews(
                    ContentParameters(
                        databasePath = database.absolutePath,
                        statement = Statements.Schema.views(query = null)
                    )
                )
                    .cells
                    .find { id == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> =
                            TableInfoColumns.values().map { it.name.lowercase() }

                        val page = getTableInfo(
                            PragmaParameters.Pragma(
                                databasePath = database.absolutePath,
                                statement = Statements.Pragma.tableInfo(name)
                            )
                        )

                        PageResponse(
                            nextPage = page.nextPage,
                            beforeCount = page.beforeCount,
                            afterCount = page.afterCount,
                            columns = headers,
                            rows = page.cells.chunked(headers.size) {
                                RowResponse(
                                    cells = it.map { cell -> cell.text.orEmpty() }
                                )
                            }
                        )
                    }
            }

    suspend fun getViewForeignKeys(
        databaseId: String,
        id: String
    ): PageResponse? =
        getDatabases(
            DatabaseParameters.Get(
                context = context, argument = null
            )
        )
            .find { databaseId == it.absolutePath.toByteArray().toSha1() }
            ?.let { database ->
                getViews(
                    ContentParameters(
                        databasePath = database.absolutePath,
                        statement = Statements.Schema.views(query = null)
                    )
                )
                    .cells
                    .find { id == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> =
                            ForeignKeyListColumns.values().map { it.name.lowercase() }

                        val page = getForeignKeys(
                            PragmaParameters.Pragma(
                                databasePath = database.absolutePath,
                                statement = Statements.Pragma.foreignKeys(name)
                            )
                        )

                        PageResponse(
                            nextPage = page.nextPage,
                            beforeCount = page.beforeCount,
                            afterCount = page.afterCount,
                            columns = headers,
                            rows = page.cells.chunked(headers.size) {
                                RowResponse(
                                    cells = it.map { cell -> cell.text.orEmpty() }
                                )
                            }
                        )
                    }
            }

    suspend fun getViewIndexes(
        databaseId: String,
        id: String
    ): PageResponse? =
        getDatabases(
            DatabaseParameters.Get(
                context = context, argument = null
            )
        )
            .find { databaseId == it.absolutePath.toByteArray().toSha1() }
            ?.let { database ->
                getViews(
                    ContentParameters(
                        databasePath = database.absolutePath,
                        statement = Statements.Schema.views(query = null)
                    )
                )
                    .cells
                    .find { id == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> =
                            IndexListColumns.values().map { it.name.lowercase() }

                        val page = getIndexes(
                            PragmaParameters.Pragma(
                                databasePath = database.absolutePath,
                                statement = Statements.Pragma.indexes(name)
                            )
                        )

                        PageResponse(
                            nextPage = page.nextPage,
                            beforeCount = page.beforeCount,
                            afterCount = page.afterCount,
                            columns = headers,
                            rows = page.cells.chunked(headers.size) {
                                RowResponse(
                                    cells = it.map { cell -> cell.text.orEmpty() }
                                )
                            }
                        )
                    }
            }
}
