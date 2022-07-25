package com.infinum.dbinspector.domain.schema.table.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters

internal class DropTableContentUseCase(
    private val connectionRepository: Repositories.Connection,
    private val schemaRepository: Repositories.Schema
) : UseCases.DropTableContent {

    override suspend fun invoke(input: ContentParameters): Page {
        val connection = connectionRepository.open(ConnectionParameters(input.databasePath))
        return schemaRepository.dropByName(input.copy(connection = connection))
    }
}
