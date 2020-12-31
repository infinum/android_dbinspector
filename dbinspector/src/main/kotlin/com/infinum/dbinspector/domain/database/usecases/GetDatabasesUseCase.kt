package com.infinum.dbinspector.domain.database.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters

internal class GetDatabasesUseCase(
    private val databaseRepository: Repositories.Database,
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetDatabases {

    override suspend fun invoke(input: DatabaseParameters.Get): List<DatabaseDescriptor> =
        databaseRepository.getPage(input)
            .filter { descriptor -> input.argument?.let { descriptor.name.contains(it) } ?: true }
            .map {
                val connection = connectionRepository.open(ConnectionParameters(databasePath = it.absolutePath))
                val version = pragmaRepository.getUserVersion(
                    PragmaParameters.Version(
                        databasePath = it.absolutePath,
                        database = connection.database,
                        statement = Statements.Pragma.userVersion()
                    )
                ).cells.firstOrNull()?.text.orEmpty()
                connectionRepository.close(ConnectionParameters(databasePath = it.absolutePath))

                it.copy(version = version)
            }
}
