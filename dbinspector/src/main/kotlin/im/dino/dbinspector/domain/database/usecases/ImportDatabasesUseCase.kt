package im.dino.dbinspector.domain.database.usecases

import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.database.models.DatabaseDescriptor
import im.dino.dbinspector.domain.database.models.Operation

internal class ImportDatabasesUseCase(
    private val databaseRepository: Repositories.Database
) : UseCases.ImportDatabases {

    override suspend fun invoke(input: Operation): List<DatabaseDescriptor> =
        databaseRepository.import(input)
}
