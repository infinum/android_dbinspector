package com.infinum.dbinspector.domain.connection.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters

internal class OpenConnectionUseCase(
    private val connectionRepository: Repositories.Connection
) : UseCases.OpenConnection {

    override suspend fun invoke(input: ConnectionParameters) {
        connectionRepository.open(input)
    }
}
