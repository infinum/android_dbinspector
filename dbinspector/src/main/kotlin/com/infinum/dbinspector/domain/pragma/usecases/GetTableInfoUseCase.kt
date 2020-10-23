package com.infinum.dbinspector.domain.pragma.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.pragma.models.TableInfoColumns
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Query

internal class GetTableInfoUseCase(
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetTableInfo {

    override suspend fun invoke(input: Query): Page {
        val connection = connectionRepository.open(input.databasePath)
        val tableInfo = pragmaRepository.getTableInfo(
            input.copy(
                database = connection
            )
        )
        return tableInfo.copy(
            fields = tableInfo.fields.filterIndexed { index, _ ->
                index % TableInfoColumns.values().size == TableInfoColumns.NAME.ordinal
            }
        )
    }
}
