package com.infinum.dbinspector.domain.pragma

import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.pragma.models.TriggerInfoColumns
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import java.util.*

internal class PragmaRepository(
    private val userVersion: Interactors.GetUserVersion,
    private val tableInfo: Interactors.GetTableInfo,
    private val foreignKeys: Interactors.GetForeignKeys,
    private val indexes: Interactors.GetIndexes,
    private val mapper: Mappers.Pragma,
    private val converter: Converters.Pragma
) : Repositories.Pragma {

    override suspend fun getUserVersion(input: PragmaParameters.Version): Page =
        mapper(userVersion(converter version input))

    override suspend fun getTableInfo(input: PragmaParameters.Info): Page =
        mapper(tableInfo(converter info input))

    override suspend fun getTriggerInfo(input: Unit): Page =
        Page(cells = TriggerInfoColumns.values()
            .map { it.name.toLowerCase(Locale.getDefault()) }
            .map(transform = mapper.transformToHeader()))

    override suspend fun getForeignKeys(input: PragmaParameters.ForeignKeys): Page =
        mapper(foreignKeys(converter foreignKeys input))

    override suspend fun getIndexes(input: PragmaParameters.Indexes): Page =
        mapper(indexes(converter indexes input))
}
