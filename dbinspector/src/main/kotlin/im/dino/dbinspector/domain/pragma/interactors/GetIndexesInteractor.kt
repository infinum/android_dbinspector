package im.dino.dbinspector.domain.pragma.interactors

import im.dino.dbinspector.data.Sources
import im.dino.dbinspector.data.models.local.QueryResult
import im.dino.dbinspector.domain.Interactors
import im.dino.dbinspector.domain.shared.models.Query

internal class GetIndexesInteractor(
    val source: Sources.Local.Pragma
) : Interactors.GetIndexes {

    override suspend fun invoke(input: Query): QueryResult =
        source.getIndexes(input)
}