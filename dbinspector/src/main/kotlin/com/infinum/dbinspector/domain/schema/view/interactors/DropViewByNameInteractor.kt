package com.infinum.dbinspector.domain.schema.view.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Interactors

internal class DropViewByNameInteractor(
    val source: Sources.Local.Schema
) : Interactors.DropViewByName {

    override suspend fun invoke(input: Query): QueryResult =
        source.dropViewByName(input)
}
