package im.dino.dbinspector.domain.database.interactors

import im.dino.dbinspector.data.Sources
import im.dino.dbinspector.domain.Interactors
import im.dino.dbinspector.domain.database.models.Operation
import java.io.File

internal class ImportDatabasesInteractor(
    val source: Sources.Raw
) : Interactors.ImportDatabases {

    override suspend fun invoke(input: Operation): List<File> =
        source.importDatabases(input)
}