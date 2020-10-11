package im.dino.dbinspector.domain.schema.table.interactors

import im.dino.dbinspector.data.Sources
import im.dino.dbinspector.data.models.local.QueryResult
import im.dino.dbinspector.data.models.local.Row
import im.dino.dbinspector.domain.Interactors
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query

internal class GetTablesInteractor(
    val source: Sources.Local.Schema
) : Interactors.GetTables {

    override suspend fun invoke(input: Query): QueryResult =
        source.getTables(input)
}