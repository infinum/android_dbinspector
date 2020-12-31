package com.infinum.dbinspector.domain.connection.control.converters

import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters

internal class ConnectionConverter : Converters.Connection {

    override suspend fun invoke(parameters: ConnectionParameters): String =
        parameters.databasePath
}
