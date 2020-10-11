package im.dino.dbinspector.domain.pragma.interactors

import im.dino.dbinspector.data.Sources
import im.dino.dbinspector.data.models.local.QueryResult
import im.dino.dbinspector.domain.Interactors
import im.dino.dbinspector.domain.shared.models.Query

internal class GetTableInfoInteractor(
    val source: Sources.Local.Pragma
) : Interactors.GetTableInfo {

    override suspend fun invoke(input: Query): QueryResult =
        source.getTableInfo(input)
}