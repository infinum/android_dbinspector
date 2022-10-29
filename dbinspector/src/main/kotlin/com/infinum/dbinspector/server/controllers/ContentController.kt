package com.infinum.dbinspector.server.controllers

import android.content.Context
import com.infinum.dbinspector.data.models.remote.RowResponse
import com.infinum.dbinspector.data.models.remote.PageResponse
import com.infinum.dbinspector.di.LibraryKoin
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.extensions.toSha1
import io.ktor.utils.io.core.toByteArray
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ContentController(
    private val context: Context
) : KoinComponent {

    private val getDatabases: UseCases.GetDatabases by inject()

    private val getTables: UseCases.GetTables by inject()
    private val getViews: UseCases.GetViews by inject()
    private val getTriggers: UseCases.GetTriggers by inject()

    private val tableInfo: UseCases.GetTableInfo by inject()

    private val getTable: UseCases.GetTable by inject()
    private val getView: UseCases.GetView by inject()
    private val getTrigger: UseCases.GetTrigger by inject()

    private val dropTable: UseCases.DropTableContent by inject()
    private val dropView: UseCases.DropView by inject()
    private val dropTrigger: UseCases.DropTrigger by inject()

    override fun getKoin(): Koin = LibraryKoin.koin()

    suspend fun getTable(
        databaseId: String,
        tableId: String,
        orderBy: String? = null,
        sort: Sort = Sort.ASCENDING
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
                    .find { tableId == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> = tableInfo(
                            PragmaParameters.Pragma(
                                databasePath = database.absolutePath,
                                statement = Statements.Pragma.tableInfo(name)
                            )
                        ).cells.map { it.text.orEmpty() }

                        val page = getTable(
                            ContentParameters(
                                databasePath = database.absolutePath,
                                statement = Statements.Schema.table(
                                    name = name,
                                    orderBy = orderBy,
                                    sort = sort
                                )
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

    suspend fun getView(
        databaseId: String,
        viewId: String,
        orderBy: String? = null,
        sort: Sort = Sort.ASCENDING
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
                    .find { viewId == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> = tableInfo(
                            PragmaParameters.Pragma(
                                databasePath = database.absolutePath,
                                statement = Statements.Pragma.tableInfo(name)
                            )
                        ).cells.map { it.text.orEmpty() }

                        val page = getView(
                            ContentParameters(
                                databasePath = database.absolutePath,
                                statement = Statements.Schema.view(
                                    name = name,
                                    orderBy = orderBy,
                                    sort = sort
                                )
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

    suspend fun getTrigger(
        databaseId: String,
        triggerId: String
    ): PageResponse? =
        getDatabases(
            DatabaseParameters.Get(
                context = context, argument = null
            )
        )
            .find { databaseId == it.absolutePath.toByteArray().toSha1() }
            ?.let { database ->
                getTriggers(
                    ContentParameters(
                        databasePath = database.absolutePath,
                        statement = Statements.Schema.triggers(query = null)
                    )
                )
                    .cells
                    .find { triggerId == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> = listOf("name", "sql")

                        val page = getTrigger(
                            ContentParameters(
                                databasePath = database.absolutePath,
                                statement = Statements.Schema.trigger(
                                    name = name
                                )
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

    suspend fun dropTable(
        databaseId: String,
        tableId: String
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
                    .find { tableId == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> = tableInfo(
                            PragmaParameters.Pragma(
                                databasePath = database.absolutePath,
                                statement = Statements.Pragma.tableInfo(name)
                            )
                        ).cells.map { it.text.orEmpty() }

                        val page = dropTable(
                            ContentParameters(
                                databasePath = database.absolutePath,
                                statement = Statements.Schema.dropTableContent(
                                    name = name
                                )
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

    suspend fun dropView(
        databaseId: String,
        viewId: String
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
                    .find { viewId == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> = tableInfo(
                            PragmaParameters.Pragma(
                                databasePath = database.absolutePath,
                                statement = Statements.Pragma.tableInfo(name)
                            )
                        ).cells.map { it.text.orEmpty() }

                        val page = dropView(
                            ContentParameters(
                                databasePath = database.absolutePath,
                                statement = Statements.Schema.dropView(
                                    name = name
                                )
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

    suspend fun dropTrigger(
        databaseId: String,
        triggerId: String
    ): PageResponse? =
        getDatabases(
            DatabaseParameters.Get(
                context = context, argument = null
            )
        )
            .find { databaseId == it.absolutePath.toByteArray().toSha1() }
            ?.let { database ->
                getTriggers(
                    ContentParameters(
                        databasePath = database.absolutePath,
                        statement = Statements.Schema.triggers(query = null)
                    )
                )
                    .cells
                    .find { triggerId == it.text?.toByteArray()?.toSha1() }
                    ?.text
                    ?.let { name ->
                        val headers: List<String> = tableInfo(
                            PragmaParameters.Pragma(
                                databasePath = database.absolutePath,
                                statement = Statements.Pragma.tableInfo(name)
                            )
                        ).cells.map { it.text.orEmpty() }

                        val page = dropTrigger(
                            ContentParameters(
                                databasePath = database.absolutePath,
                                statement = Statements.Schema.dropTrigger(
                                    name = name
                                )
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