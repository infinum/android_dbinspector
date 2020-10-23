package com.infinum.dbinspector.domain.connection.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases

internal class OpenConnectionUseCase(
    private val connectionRepository: Repositories.Connection
) : UseCases.OpenConnection {

    override suspend fun invoke(input: String) {
        connectionRepository.open(input)
    }
}
