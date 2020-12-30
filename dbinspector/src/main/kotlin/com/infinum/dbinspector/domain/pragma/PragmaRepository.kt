package com.infinum.dbinspector.domain.pragma

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.pragma.models.TriggerInfoColumns
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import java.util.Locale

internal class PragmaRepository(
    private val userVersion: Interactors.GetUserVersion,
    private val tableInfo: Interactors.GetTableInfo,
    private val foreignKeys: Interactors.GetForeignKeys,
    private val indexes: Interactors.GetIndexes,
    private val mapper: Mappers.Pragma
) : Repositories.Pragma {

    override suspend fun getUserVersion(input: PragmaParameters.Version): Page =
        userVersion(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement
            )
        ).let { mapper.mapLocalToDomain(it) }

    override suspend fun getTableInfo(input: PragmaParameters.Info): Page =
        tableInfo(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = mapper.sortMapper().mapDomainToLocal(input.sort),
                pageSize = input.pageSize,
                page = input.page
            )
        ).let { mapper.mapLocalToDomain(it) }

    override suspend fun getTriggerInfo(input: PragmaParameters.Info): Page =
        Page(cells = TriggerInfoColumns.values()
            .map { it.name.toLowerCase(Locale.getDefault()) }
            .map(transform = mapper.transformToHeader()))

    override suspend fun getForeignKeys(input: PragmaParameters.ForeignKeys): Page =
        foreignKeys(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = mapper.sortMapper().mapDomainToLocal(input.sort),
                pageSize = input.pageSize,
                page = input.page
            )
        ).let { mapper.mapLocalToDomain(it) }

    override suspend fun getIndexes(input: PragmaParameters.Indexes): Page =
        indexes(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = mapper.sortMapper().mapDomainToLocal(input.sort),
                pageSize = input.pageSize,
                page = input.page
            )
        ).let { mapper.mapLocalToDomain(it) }
}
