package com.infinum.dbinspector.domain.database

import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters

internal class DatabaseRepository(
    private val getPageInteractor: Interactors.GetDatabases,
    private val importInteractor: Interactors.ImportDatabases,
    private val removeInteractor: Interactors.RemoveDatabase,
    private val renameInteractor: Interactors.RenameDatabase,
    private val copyInteractor: Interactors.CopyDatabase,
    private val mapper: Mappers.DatabaseDescriptor,
    private val converter: Converters.Database
) : Repositories.Database {

    override suspend fun getPage(input: DatabaseParameters.Get): List<DatabaseDescriptor> =
        getPageInteractor(converter get input).map { mapper(it) }

    override suspend fun import(input: DatabaseParameters.Import): List<DatabaseDescriptor> =
        importInteractor(converter import input).map { mapper(it) }

    override suspend fun rename(input: DatabaseParameters.Rename): List<DatabaseDescriptor> =
        renameInteractor(converter rename input).map { mapper(it) }

    override suspend fun remove(input: DatabaseParameters.Command): List<DatabaseDescriptor> =
        removeInteractor(converter command input).map { mapper(it) }

    override suspend fun copy(input: DatabaseParameters.Command): List<DatabaseDescriptor> =
        copyInteractor(converter command input).map { mapper(it) }
}
