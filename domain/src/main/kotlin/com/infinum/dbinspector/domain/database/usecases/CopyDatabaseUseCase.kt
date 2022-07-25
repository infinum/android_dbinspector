package com.infinum.dbinspector.domain.database.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters

internal class CopyDatabaseUseCase(
    private val databaseRepository: Repositories.Database
) : UseCases.CopyDatabase {

    override suspend fun invoke(input: DatabaseParameters.Command): List<DatabaseDescriptor> =
        databaseRepository.copy(input)
}
