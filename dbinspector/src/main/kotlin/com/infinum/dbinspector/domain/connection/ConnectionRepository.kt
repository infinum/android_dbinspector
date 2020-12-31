package com.infinum.dbinspector.domain.connection

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.connection.models.DatabaseConnection
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters

internal class ConnectionRepository(
    private val openInteractor: Interactors.OpenConnection,
    private val closeInteractor: Interactors.CloseConnection,
    private val control: Control.Connection
) : Repositories.Connection {

    override suspend fun open(input: ConnectionParameters): DatabaseConnection =
        control.mapper(openInteractor(control.converter(input)))

    override suspend fun close(input: ConnectionParameters) =
        closeInteractor(control.converter(input))
}
