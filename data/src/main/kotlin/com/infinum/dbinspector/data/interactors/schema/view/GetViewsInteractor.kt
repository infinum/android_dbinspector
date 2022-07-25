package com.infinum.dbinspector.data.interactors.schema.view

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.Interactors

internal class GetViewsInteractor(
    private val source: com.infinum.dbinspector.data.Sources.Local.Schema
) : Interactors.GetViews {

    override suspend fun invoke(input: Query): QueryResult =
        source.getViews(input)
}
