package com.infinum.dbinspector.domain.database

import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters

internal class DatabaseRepository(
    private val getPageInteractor: Interactors.GetDatabases,
    private val importInteractor: Interactors.ImportDatabases,
    private val removeInteractor: Interactors.RemoveDatabase,
    private val renameInteractor: Interactors.RenameDatabase,
    private val copyInteractor: Interactors.CopyDatabase
) : Repositories.Database {

    override suspend fun getPage(input: DatabaseParameters.Get): List<DatabaseDescriptor> =
        getPageInteractor(
            Operation(
                context = input.context,
                argument = input.argument
            )
        ).map {
            DatabaseDescriptor(
                it.exists(),
                it.parentFile?.absolutePath.orEmpty(),
                it.nameWithoutExtension,
                extension = it.extension
            )
        }

    override suspend fun import(input: DatabaseParameters.Import): List<DatabaseDescriptor> =
        importInteractor(
            Operation(
                context = input.context,
                importUris = input.importUris
            )
        ).map {
            DatabaseDescriptor(
                it.exists(),
                it.parentFile?.absolutePath.orEmpty(),
                it.nameWithoutExtension,
                extension = it.extension
            )
        }

    override suspend fun remove(input: DatabaseParameters.Remove): List<DatabaseDescriptor> =
        removeInteractor(
            Operation(
                context = input.context,
                databaseDescriptor = input.databaseDescriptor
            )
        ).map {
            DatabaseDescriptor(
                it.exists(),
                it.parentFile?.absolutePath.orEmpty(),
                it.nameWithoutExtension,
                extension = it.extension
            )
        }

    override suspend fun rename(input: DatabaseParameters.Rename): List<DatabaseDescriptor> =
        renameInteractor(
            Operation(
                context = input.context,
                databaseDescriptor = input.databaseDescriptor,
                argument = input.argument
            )
        ).map {
            DatabaseDescriptor(
                it.exists(),
                it.parentFile?.absolutePath.orEmpty(),
                it.nameWithoutExtension,
                extension = it.extension
            )
        }

    override suspend fun copy(input: DatabaseParameters.Copy): List<DatabaseDescriptor> =
        copyInteractor(
            Operation(
                context = input.context,
                databaseDescriptor = input.databaseDescriptor
            )
        ).map {
            DatabaseDescriptor(
                it.exists(),
                it.parentFile?.absolutePath.orEmpty(),
                it.nameWithoutExtension,
                extension = it.extension
            )
        }
}
