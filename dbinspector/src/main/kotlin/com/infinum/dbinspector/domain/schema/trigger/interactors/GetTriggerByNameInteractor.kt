package com.infinum.dbinspector.domain.schema.trigger.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Interactors

internal class GetTriggerByNameInteractor(
    val source: Sources.Local.Schema
) : Interactors.GetTriggerByName {

    override suspend fun invoke(input: Query): QueryResult =
        source.getTriggerByName(input)
}
