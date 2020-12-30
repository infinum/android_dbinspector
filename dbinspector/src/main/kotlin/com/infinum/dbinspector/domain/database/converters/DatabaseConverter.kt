package com.infinum.dbinspector.domain.database.converters

import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters

internal class DatabaseConverter : Converters.Database {

    override suspend fun get(parameters: DatabaseParameters.Get): Operation =
        Operation(
            context = parameters.context,
            argument = parameters.argument
        )

    override suspend fun import(parameters: DatabaseParameters.Import): Operation =
        Operation(
            context = parameters.context,
            importUris = parameters.importUris
        )

    override suspend fun rename(parameters: DatabaseParameters.Rename): Operation =
        Operation(
            context = parameters.context,
            databaseDescriptor = parameters.databaseDescriptor,
            argument = parameters.argument
        )

    override suspend fun command(parameters: DatabaseParameters.Command): Operation =
        Operation(
            context = parameters.context,
            databaseDescriptor = parameters.databaseDescriptor
        )
}