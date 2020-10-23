package com.infinum.dbinspector.domain.schema.table.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.QueryResult
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.shared.models.Query

internal class DropTableContentByNameInteractor(
    val source: Sources.Local.Schema
) : Interactors.DropTableContentByName {

    override suspend fun invoke(input: Query): QueryResult =
        source.dropTableContentByName(input)
}
