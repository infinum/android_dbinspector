package com.infinum.dbinspector.domain.schema.table.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Interactors

internal class GetTablesInteractor(
    private val source: Sources.Local.Schema
) : Interactors.GetTables {

    override suspend fun invoke(input: Query): QueryResult =
        source.getTables(input)
}
