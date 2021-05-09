package com.infinum.dbinspector.domain.schema.trigger.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Interactors

internal class DropTriggerByNameInteractor(
    private val source: Sources.Local.Schema
) : Interactors.DropTriggerByName {

    override suspend fun invoke(input: Query): QueryResult =
        source.dropTriggerByName(input)
}
