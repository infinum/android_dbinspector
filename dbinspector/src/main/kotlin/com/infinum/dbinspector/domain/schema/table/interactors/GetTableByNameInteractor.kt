package com.infinum.dbinspector.domain.schema.table.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Interactors

internal class GetTableByNameInteractor(
    private val source: Sources.Local.Schema
) : Interactors.GetTableByName {

    override suspend fun invoke(input: Query): QueryResult =
        source.getTableByName(input)
}
