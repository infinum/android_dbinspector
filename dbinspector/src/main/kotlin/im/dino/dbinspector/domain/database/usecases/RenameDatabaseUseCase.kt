package im.dino.dbinspector.domain.database.usecases

import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.database.models.DatabaseDescriptor
import im.dino.dbinspector.domain.database.models.Operation

internal class RenameDatabaseUseCase(
    private val databaseRepository: Repositories.Database
) : UseCases.RenameDatabase {

    override suspend fun invoke(input: Operation): List<DatabaseDescriptor> =
        databaseRepository.rename(input)
}
