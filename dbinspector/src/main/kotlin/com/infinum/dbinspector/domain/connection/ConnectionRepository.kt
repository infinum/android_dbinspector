package com.infinum.dbinspector.domain.connection

import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.connection.models.DatabaseConnection
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters

internal class ConnectionRepository(
    private val openInteractor: Interactors.OpenConnection,
    private val closeInteractor: Interactors.CloseConnection,
    private val mapper: Mappers.Connection,
    private val converter: Converters.Connection
) : Repositories.Connection {

    override suspend fun open(input: ConnectionParameters): DatabaseConnection =
        mapper(openInteractor(converter(input)))

    override suspend fun close(input: ConnectionParameters) =
        closeInteractor(converter(input))
}
