package im.dino.dbinspector.domain.connection.usecases

import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.UseCases

internal class CloseConnectionUseCase(
    private val connectionRepository: Repositories.Connection
) : UseCases.CloseConnection {

    override suspend fun invoke(input: String) =
        connectionRepository.close(input)
}
