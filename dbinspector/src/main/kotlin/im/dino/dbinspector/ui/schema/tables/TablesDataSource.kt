package im.dino.dbinspector.ui.schema.tables

import im.dino.dbinspector.domain.schema.table.AllTablesOperation
import im.dino.dbinspector.ui.schema.shared.SchemaDataSource

internal class TablesDataSource(
    path: String,
    pageSize: Int,
    argument: String?,
    onEmpty: suspend (value: Boolean) -> Unit
) : SchemaDataSource(path, onEmpty) {

    override val source = lazyOf(AllTablesOperation(pageSize, argument))
}
