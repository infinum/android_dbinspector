package com.infinum.dbinspector.domain.pragma.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters

internal class GetForeignKeysUseCase(
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetForeignKeys {

    override suspend fun invoke(input: PragmaParameters.Pragma): Page {
        val connection = connectionRepository.open(ConnectionParameters(input.databasePath))
        return pragmaRepository.getForeignKeys(input.copy(connection = connection))
    }
}
