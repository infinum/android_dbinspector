package im.dino.dbinspector.domain.schema.view.interactors

import im.dino.dbinspector.data.Sources
import im.dino.dbinspector.data.models.local.QueryResult
import im.dino.dbinspector.domain.Interactors
import im.dino.dbinspector.domain.shared.models.Query

internal class DropViewByNameInteractor(
    val source: Sources.Local.Schema
) : Interactors.DropViewByName {

    override suspend fun invoke(input: Query): QueryResult =
        source.dropViewByName(input)
}
