package com.infinum.dbinspector.domain.connection.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases

internal class CloseConnectionUseCase(
    private val connectionRepository: Repositories.Connection
) : UseCases.CloseConnection {

    override suspend fun invoke(input: String) =
        connectionRepository.close(input)
}
