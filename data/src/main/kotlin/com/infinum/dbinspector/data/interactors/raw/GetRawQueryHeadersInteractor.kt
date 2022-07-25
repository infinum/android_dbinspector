package com.infinum.dbinspector.data.interactors.raw

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.Interactors

internal class GetRawQueryHeadersInteractor(
    private val source: com.infinum.dbinspector.data.Sources.Local.RawQuery
) : Interactors.GetRawQueryHeaders {

    override suspend fun invoke(input: Query): QueryResult =
        source.rawQueryHeaders(input)
}
