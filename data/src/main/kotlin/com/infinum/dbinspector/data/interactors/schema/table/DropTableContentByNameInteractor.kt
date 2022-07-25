package com.infinum.dbinspector.data.interactors.schema.table

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.Interactors

internal class DropTableContentByNameInteractor(
    private val source: com.infinum.dbinspector.data.Sources.Local.Schema
) : Interactors.DropTableContentByName {

    override suspend fun invoke(input: Query): QueryResult =
        source.dropTableContentByName(input)
}
