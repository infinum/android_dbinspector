package com.infinum.dbinspector.ui.server.controllers

import android.content.Context
import com.infinum.dbinspector.data.models.remote.SchemaPageResponse
import com.infinum.dbinspector.data.models.remote.SchemaResponse
import com.infinum.dbinspector.di.LibraryKoin
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.extensions.toSha1
import io.ktor.utils.io.core.toByteArray
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class SchemaController(
    private val context: Context
) : KoinComponent {

    private val getDatabases: UseCases.GetDatabases by inject()

    private val getTables: UseCases.GetTables by inject()
    private val getViews: UseCases.GetViews by inject()
    private val getTriggers: UseCases.GetTriggers by inject()

    override fun getKoin(): Koin = LibraryKoin.koin()

    suspend fun getTablesById(id: String, query: String?): SchemaPageResponse? =
        getDatabases(
            DatabaseParameters.Get(
                context = context, argument = null
            )
        ).find { id == it.absolutePath.toByteArray().toSha1() }?.let {
            getTables(
                ContentParameters(
                    databasePath = it.absolutePath,
                    statement = Statements.Schema.tables(query = query)
                )
            )
        }?.let { page ->
            SchemaPageResponse(nextPage = page.nextPage,
                beforeCount = page.beforeCount,
                afterCount = page.afterCount,
                cells = page.cells.map { cell ->
                    SchemaResponse(
                        id = cell.text?.toByteArray()?.toSha1(),
                        name = cell.text
                    )
                })
        }

    suspend fun getViewsById(id: String, query: String?): SchemaPageResponse? =
        getDatabases(
            DatabaseParameters.Get(
                context = context, argument = null
            )
        ).find { id == it.absolutePath.toByteArray().toSha1() }?.let {
            getViews(
                ContentParameters(
                    databasePath = it.absolutePath,
                    statement = Statements.Schema.views(query = query)
                )
            )
        }?.let { page ->
            SchemaPageResponse(nextPage = page.nextPage,
                beforeCount = page.beforeCount,
                afterCount = page.afterCount,
                cells = page.cells.map { cell ->
                    SchemaResponse(
                        id = cell.text?.toByteArray()?.toSha1(),
                        name = cell.text
                    )
                })
        }

    suspend fun getTriggersById(id: String, query: String?): SchemaPageResponse? =
        getDatabases(
            DatabaseParameters.Get(
                context = context, argument = null
            )
        ).find { id == it.absolutePath.toByteArray().toSha1() }?.let {
            getTriggers(
                ContentParameters(
                    databasePath = it.absolutePath,
                    statement = Statements.Schema.triggers(query = query)
                )
            )
        }?.let { page ->
            SchemaPageResponse(
                nextPage = page.nextPage,
                beforeCount = page.beforeCount,
                afterCount = page.afterCount,
                cells = page.cells.map { cell ->
                    SchemaResponse(
                        id = cell.text?.toByteArray()?.toSha1(),
                        name = cell.text
                    )
                })
        }
}
