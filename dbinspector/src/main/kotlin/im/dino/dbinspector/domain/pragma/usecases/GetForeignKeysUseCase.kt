package im.dino.dbinspector.domain.pragma.usecases

import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query

internal class GetForeignKeysUseCase(
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetForeignKeys {

    override suspend fun invoke(input: Query): Page {
        val connection = connectionRepository.open(input.databasePath)
        return pragmaRepository.getForeignKeys(
            input.copy(
                database = connection
            )
        )
    }
}