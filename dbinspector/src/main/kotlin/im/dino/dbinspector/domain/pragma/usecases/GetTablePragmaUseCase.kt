package im.dino.dbinspector.domain.pragma.usecases

import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query

internal class GetTablePragmaUseCase(
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetTablePragma {

    override suspend fun invoke(input: Query): Page {
        val connection = connectionRepository.open(input.databasePath)
        return pragmaRepository.getTableInfo(
            input.copy(
                database = connection
            )
        )
    }
}