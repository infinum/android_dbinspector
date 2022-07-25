package com.infinum.dbinspector.domain.database

import com.infinum.dbinspector.data.Interactors
import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters

internal class DatabaseRepository(
    private val getPageInteractor: Interactors.GetDatabases,
    private val importInteractor: Interactors.ImportDatabases,
    private val removeInteractor: Interactors.RemoveDatabase,
    private val renameInteractor: Interactors.RenameDatabase,
    private val copyInteractor: Interactors.CopyDatabase,
    private val control: Control.Database
) : Repositories.Database {

    override suspend fun getPage(input: DatabaseParameters.Get): List<DatabaseDescriptor> =
        getPageInteractor(control.converter get input).map { control.mapper(it) }

    override suspend fun import(input: DatabaseParameters.Import): List<DatabaseDescriptor> =
        importInteractor(control.converter import input).map { control.mapper(it) }

    override suspend fun rename(input: DatabaseParameters.Rename): List<DatabaseDescriptor> =
        renameInteractor(control.converter rename input).map { control.mapper(it) }

    override suspend fun remove(input: DatabaseParameters.Command): List<DatabaseDescriptor> =
        removeInteractor(control.converter command input).map { control.mapper(it) }

    override suspend fun copy(input: DatabaseParameters.Command): List<DatabaseDescriptor> =
        copyInteractor(control.converter command input).map { control.mapper(it) }
}
