package com.infinum.dbinspector.domain.database.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.database.models.Operation
import java.io.File

internal class ImportDatabasesInteractor(
    val source: Sources.Raw
) : Interactors.ImportDatabases {

    override suspend fun invoke(input: Operation): List<File> =
        source.importDatabases(input)
}
