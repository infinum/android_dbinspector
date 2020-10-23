package com.infinum.dbinspector.domain.pragma.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Query

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
