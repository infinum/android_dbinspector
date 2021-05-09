package com.infinum.dbinspector.domain.raw.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Interactors

internal class GetRawQueryHeadersInteractor(
    private val source: Sources.Local.RawQuery
) : Interactors.GetRawQueryHeaders {

    override suspend fun invoke(input: Query): QueryResult =
        source.rawQueryHeaders(input)
}
