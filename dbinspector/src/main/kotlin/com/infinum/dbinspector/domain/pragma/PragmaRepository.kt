package com.infinum.dbinspector.domain.pragma

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.pragma.models.TriggerInfoColumns
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.extensions.lowercase

internal class PragmaRepository(
    private val userVersion: Interactors.GetUserVersion,
    private val tableInfo: Interactors.GetTableInfo,
    private val foreignKeys: Interactors.GetForeignKeys,
    private val indexes: Interactors.GetIndexes,
    private val control: Control.Pragma
) : Repositories.Pragma {

    override suspend fun getUserVersion(input: PragmaParameters.Version): Page =
        control.mapper(userVersion(control.converter version input))

    override suspend fun getTableInfo(input: PragmaParameters.Pragma): Page =
        control.mapper(tableInfo(control.converter pragma input))

    override suspend fun getTriggerInfo(): Page =
        Page(
            cells = TriggerInfoColumns.values()
                .map { it.name.lowercase() }
                .map(transform = control.mapper.transformToHeader())
        )

    override suspend fun getForeignKeys(input: PragmaParameters.Pragma): Page =
        control.mapper(foreignKeys(control.converter pragma input))

    override suspend fun getIndexes(input: PragmaParameters.Pragma): Page =
        control.mapper(indexes(control.converter pragma input))
}
