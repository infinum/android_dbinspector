package im.dino.dbinspector.ui.schema.tables

import im.dino.dbinspector.domain.schema.table.AllTablesOperation
import im.dino.dbinspector.ui.schema.shared.SchemaDataSource

internal class TablesDataSource(
    path: String,
    private val pageSize: Int,
    private val args: String?,
    empty: suspend (value: Boolean) -> Unit
) : SchemaDataSource(path, empty) {

    override val source = lazyOf(AllTablesOperation(pageSize, args))
}