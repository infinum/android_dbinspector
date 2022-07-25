package com.infinum.dbinspector.data.interactors.connection

import com.infinum.dbinspector.data.Interactors

internal class CloseConnectionInteractor(
    private val source: com.infinum.dbinspector.data.Sources.Memory.Connection
) : Interactors.CloseConnection {

    override suspend fun invoke(input: String) =
        source.closeConnection(input)
}
