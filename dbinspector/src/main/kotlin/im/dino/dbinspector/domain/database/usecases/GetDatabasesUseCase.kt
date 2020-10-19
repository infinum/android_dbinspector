package im.dino.dbinspector.domain.database.usecases

import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.database.models.DatabaseDescriptor
import im.dino.dbinspector.domain.database.models.Operation
import im.dino.dbinspector.domain.shared.models.Query
import im.dino.dbinspector.domain.shared.models.Statements

internal class GetDatabasesUseCase(
    private val databaseRepository: Repositories.Database,
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetDatabases {

    override suspend fun invoke(input: Operation): List<DatabaseDescriptor> =
        databaseRepository.getPage(input)
            .filter { descriptor -> input.argument?.let { descriptor.name.contains(it) } ?: true }
            .map {
                val database = connectionRepository.open(it.absolutePath)

                val descriptorWithVersion = it.copy(
                    version = pragmaRepository.getUserVersion(
                        Query(
                            databasePath = it.absolutePath,
                            database = database,
                            statement = Statements.Pragma.userVersion()
                        )
                    ).fields.first()
                )
                connectionRepository.close(it.absolutePath)

                descriptorWithVersion
            }
}
