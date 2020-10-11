package im.dino.dbinspector.domain.connection.interactors

import im.dino.dbinspector.data.Sources
import im.dino.dbinspector.domain.Interactors

internal class CloseConnectionInteractor(
    val source: Sources.Memory
) : Interactors.CloseConnection {

    override suspend fun invoke(input: String) =
        source.closeConnection(input)
}