package com.infinum.dbinspector.domain.raw.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters

internal class GetAffectedRowsUseCase(
    private val connectionRepository: Repositories.Connection,
    private val rawQueryRepository: Repositories.RawQuery
) : UseCases.GetAffectedRows {

    override suspend fun invoke(input: ContentParameters): Page {
        val connection = connectionRepository.open(ConnectionParameters(input.databasePath))
        return rawQueryRepository.getAffectedRows(
            input.copy(
                connection = connection
            )
        )
    }
}
