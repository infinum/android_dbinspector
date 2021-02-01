package com.infinum.dbinspector.domain.pragma.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Interactors

internal class GetUserVersionInteractor(
    val source: Sources.Local.Pragma
) : Interactors.GetUserVersion {

    override suspend fun invoke(input: Query): QueryResult =
        source.getUserVersion(input)
}
