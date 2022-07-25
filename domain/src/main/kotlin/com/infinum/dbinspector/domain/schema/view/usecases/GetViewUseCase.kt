package com.infinum.dbinspector.domain.schema.view.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters

internal class GetViewUseCase(
    private val connectionRepository: Repositories.Connection,
    private val schemaRepository: Repositories.Schema
) : UseCases.GetView {

    override suspend fun invoke(input: ContentParameters): Page {
        val connection = connectionRepository.open(ConnectionParameters(input.databasePath))
        return schemaRepository.getByName(
            input.copy(
                connection = connection
            )
        )
    }
}
