package com.infinum.dbinspector.data.interactors.schema.table

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.Interactors

internal class GetTablesInteractor(
    private val source: com.infinum.dbinspector.data.Sources.Local.Schema
) : Interactors.GetTables {

    override suspend fun invoke(input: Query): QueryResult =
        source.getTables(input)
}
