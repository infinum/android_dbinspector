package com.infinum.dbinspector.domain.database.control.converters

import com.infinum.dbinspector.data.models.raw.DatabaseDescriptorTask
import com.infinum.dbinspector.data.models.raw.OperationTask
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters

internal class DatabaseConverter : Converters.Database {

    override suspend fun get(parameters: DatabaseParameters.Get): OperationTask =
        OperationTask(
            context = parameters.context,
            argument = parameters.argument
        )

    override suspend fun import(parameters: DatabaseParameters.Import): OperationTask =
        OperationTask(
            context = parameters.context,
            importUris = parameters.importUris
        )

    override suspend fun rename(parameters: DatabaseParameters.Rename): OperationTask =
        OperationTask(
            context = parameters.context,
            databaseDescriptor = DatabaseDescriptorTask(
                exists = parameters.databaseDescriptor.exists,
                parentPath = parameters.databaseDescriptor.parentPath,
                name = parameters.databaseDescriptor.name,
                extension = parameters.databaseDescriptor.extension,
                version = parameters.databaseDescriptor.version
            ),
            argument = parameters.argument
        )

    override suspend fun command(parameters: DatabaseParameters.Command): OperationTask =
        OperationTask(
            context = parameters.context,
            databaseDescriptor = DatabaseDescriptorTask(
                exists = parameters.databaseDescriptor.exists,
                parentPath = parameters.databaseDescriptor.parentPath,
                name = parameters.databaseDescriptor.name,
                extension = parameters.databaseDescriptor.extension,
                version = parameters.databaseDescriptor.version
            )
        )
}
