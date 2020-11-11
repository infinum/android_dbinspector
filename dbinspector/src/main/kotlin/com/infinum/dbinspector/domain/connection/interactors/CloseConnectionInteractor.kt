package com.infinum.dbinspector.domain.connection.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.domain.Interactors

internal class CloseConnectionInteractor(
    val source: Sources.Memory
) : Interactors.CloseConnection {

    override suspend fun invoke(input: String) =
        source.closeConnection(input)
}
