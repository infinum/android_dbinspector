package com.infinum.dbinspector.domain.pragma

import com.infinum.dbinspector.data.models.local.cursor.Order
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.data.models.local.cursor.Query
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters

internal class PragmaRepository(
    private val userVersion: Interactors.GetUserVersion,
    private val tableInfo: Interactors.GetTableInfo,
    private val foreignKeys: Interactors.GetForeignKeys,
    private val indexes: Interactors.GetIndexes,
    private val mapper: Mappers.PragmaCell
) : Repositories.Pragma {

    override suspend fun getUserVersion(input: PragmaParameters.Version): Page =
        userVersion(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement
            )
        ).let {
            Page(
                cells = it.rows.map { row ->
                    row.fields.toList().map { field -> mapper.toDomain(field) }
                }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun getTableInfo(input: PragmaParameters.Info): Page =
        tableInfo(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = Order(input.sort.rawValue),
                pageSize = input.pageSize,
                page = input.page
            )
        ).let {
            Page(
                cells = it.rows.map { row ->
                    row.fields.toList().map { field -> mapper.toDomain(field) }
                }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun getTriggerInfo(input: PragmaParameters.Info): Page =
        Page(
            cells = input.columns.map {
                Cell(text = it)
            }
        )

    override suspend fun getForeignKeys(input: PragmaParameters.ForeignKeys): Page =
        foreignKeys(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = Order(input.sort.rawValue),
                pageSize = input.pageSize,
                page = input.page
            )
        ).let {
            Page(
                cells = it.rows.map { row ->
                    row.fields.toList().map { field -> mapper.toDomain(field) }
                }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun getIndexes(input: PragmaParameters.Indexes): Page =
        indexes(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = Order(input.sort.rawValue),
                pageSize = input.pageSize,
                page = input.page
            )
        ).let {
            Page(
                cells = it.rows.map { row ->
                    row.fields.toList().map { field -> mapper.toDomain(field) }
                }.flatten(),
                nextPage = it.nextPage
            )
        }
}
