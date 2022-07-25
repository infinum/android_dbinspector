package com.infinum.dbinspector.data.interactors.schema.trigger

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.Interactors

internal class DropTriggerByNameInteractor(
    private val source: com.infinum.dbinspector.data.Sources.Local.Schema
) : Interactors.DropTriggerByName {

    override suspend fun invoke(input: Query): QueryResult =
        source.dropTriggerByName(input)
}
