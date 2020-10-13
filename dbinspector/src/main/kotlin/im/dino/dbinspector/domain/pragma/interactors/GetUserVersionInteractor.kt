package im.dino.dbinspector.domain.pragma.interactors

import im.dino.dbinspector.data.Sources
import im.dino.dbinspector.data.models.local.QueryResult
import im.dino.dbinspector.domain.Interactors
import im.dino.dbinspector.domain.shared.models.Query

internal class GetUserVersionInteractor(
    val source: Sources.Local.Pragma
) : Interactors.GetUserVersion {

    override suspend fun invoke(input: Query): QueryResult =
        source.getUserVersion(input)
}
