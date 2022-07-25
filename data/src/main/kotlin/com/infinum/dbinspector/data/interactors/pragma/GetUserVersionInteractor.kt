package com.infinum.dbinspector.data.interactors.pragma

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.Interactors

internal class GetUserVersionInteractor(
    private val source: com.infinum.dbinspector.data.Sources.Local.Pragma
) : Interactors.GetUserVersion {

    override suspend fun invoke(input: Query): QueryResult =
        source.getUserVersion(input)
}
