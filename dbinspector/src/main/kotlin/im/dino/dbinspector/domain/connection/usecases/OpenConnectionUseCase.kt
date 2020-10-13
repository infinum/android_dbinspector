package im.dino.dbinspector.domain.connection.usecases

import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.UseCases

internal class OpenConnectionUseCase(
    private val connectionRepository: Repositories.Connection
) : UseCases.OpenConnection {

    override suspend fun invoke(input: String) {
        connectionRepository.open(input)
    }
}
