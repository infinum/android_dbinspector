package com.infinum.dbinspector.domain.pragma.control.converters

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters

internal class PragmaConverter(
    private val sortConverter: Converters.Sort
) : Converters.Pragma {

    override suspend fun version(parameters: PragmaParameters.Version): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.database,
            statement = parameters.statement
        )

    override suspend fun info(parameters: PragmaParameters.Info): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.database,
            statement = parameters.statement,
            order = sortConverter(parameters.sort),
            pageSize = parameters.pageSize,
            page = parameters.page
        )

    override suspend fun foreignKeys(parameters: PragmaParameters.ForeignKeys): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.database,
            statement = parameters.statement,
            order = sortConverter(parameters.sort),
            pageSize = parameters.pageSize,
            page = parameters.page
        )

    override suspend fun indexes(parameters: PragmaParameters.Indexes): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.database,
            statement = parameters.statement,
            order = sortConverter(parameters.sort),
            pageSize = parameters.pageSize,
            page = parameters.page
        )
}
