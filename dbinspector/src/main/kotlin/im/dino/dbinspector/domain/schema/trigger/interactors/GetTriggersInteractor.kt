package im.dino.dbinspector.domain.schema.trigger.interactors

import im.dino.dbinspector.data.Sources
import im.dino.dbinspector.data.models.local.QueryResult
import im.dino.dbinspector.domain.Interactors
import im.dino.dbinspector.domain.shared.models.Query

internal class GetTriggersInteractor(
    val source: Sources.Local.Schema
) : Interactors.GetTriggers {

    override suspend fun invoke(input: Query): QueryResult =
        source.getTriggers(input)
}