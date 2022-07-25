package com.infinum.dbinspector.domain.pragma.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.pragma.models.TableInfoColumns
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters

internal class GetTableInfoUseCase(
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetTableInfo {

    override suspend fun invoke(input: PragmaParameters.Pragma): Page {
        val connection = connectionRepository.open(ConnectionParameters(input.databasePath))
        val tableInfo = pragmaRepository.getTableInfo(input.copy(connection = connection))
        return tableInfo.copy(
            cells = tableInfo.cells.filterIndexed { index, _ ->
                index % TableInfoColumns.values().size == TableInfoColumns.NAME.ordinal
            }
        )
    }
}
