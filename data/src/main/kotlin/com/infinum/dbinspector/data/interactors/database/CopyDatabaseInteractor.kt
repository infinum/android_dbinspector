package com.infinum.dbinspector.data.interactors.database

import com.infinum.dbinspector.data.Interactors
import com.infinum.dbinspector.data.models.raw.OperationTask
import java.io.File

internal class CopyDatabaseInteractor(
    private val source: com.infinum.dbinspector.data.Sources.Raw
) : Interactors.CopyDatabase {

    override suspend fun invoke(input: OperationTask): List<File> =
        source.copyDatabase(input)
}
