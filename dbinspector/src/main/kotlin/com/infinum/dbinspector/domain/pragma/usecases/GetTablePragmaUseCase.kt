package com.infinum.dbinspector.domain.pragma.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Query
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters

internal class GetTablePragmaUseCase(
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetTablePragma {

    override suspend fun invoke(input: PragmaParameters.Info): Page {
        val connection = connectionRepository.open(ConnectionParameters(input.databasePath))
        return pragmaRepository.getTableInfo(input.copy(database = connection))
    }
}
