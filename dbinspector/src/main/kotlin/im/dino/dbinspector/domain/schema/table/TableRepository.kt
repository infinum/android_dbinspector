package im.dino.dbinspector.domain.schema.table

import im.dino.dbinspector.domain.Interactors
import im.dino.dbinspector.domain.schema.shared.AbstractSchemaRepository

internal class TableRepository(
    getPage: Interactors.GetTables,
    getByName: Interactors.GetTableByName,
    dropByName: Interactors.DropTableContentByName
) : AbstractSchemaRepository(
    getPage,
    getByName,
    dropByName
)
