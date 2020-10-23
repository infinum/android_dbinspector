package com.infinum.dbinspector.domain.pragma.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Query

internal class GetIndexesUseCase(
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetIndexes {

    override suspend fun invoke(input: Query): Page {
        val connection = connectionRepository.open(input.databasePath)
        return pragmaRepository.getIndexes(
            input.copy(
                database = connection
            )
        )
    }
}
