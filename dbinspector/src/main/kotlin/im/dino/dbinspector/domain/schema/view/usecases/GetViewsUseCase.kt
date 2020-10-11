package im.dino.dbinspector.domain.schema.view.usecases

import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query

internal class GetViewsUseCase(
    private val connectionRepository: Repositories.Connection,
    private val schemaRepository: Repositories.Schema
) : UseCases.GetViews {

    override suspend fun invoke(input: Query): Page {
        val connection = connectionRepository.open(input.databasePath)
        return schemaRepository.getPage(
            input.copy(
                database = connection
            )
        )
    }
}