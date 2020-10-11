package im.dino.dbinspector.domain.schema.trigger.usecases

import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query

internal class GetTriggersUseCase(
    private val connectionRepository: Repositories.Connection,
    private val schemaRepository: Repositories.Schema
) : UseCases.GetTriggers {

    override suspend fun invoke(input: Query): Page {
        val connection = connectionRepository.open(input.databasePath)
        return schemaRepository.getPage(
            input.copy(
                database = connection
            )
        )
    }
}