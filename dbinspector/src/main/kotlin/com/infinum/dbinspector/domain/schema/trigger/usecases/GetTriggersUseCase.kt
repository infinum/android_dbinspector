package com.infinum.dbinspector.domain.schema.trigger.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Query

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
