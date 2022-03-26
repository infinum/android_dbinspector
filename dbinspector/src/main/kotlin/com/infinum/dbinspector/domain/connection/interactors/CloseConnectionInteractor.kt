package com.infinum.dbinspector.domain.connection.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.domain.Interactors

internal class CloseConnectionInteractor(
    private val source: Sources.Memory.Connection
) : Interactors.CloseConnection {

    override suspend fun invoke(input: String) =
        source.closeConnection(input)
}
