package im.dino.dbinspector.ui.content.table

import im.dino.dbinspector.domain.pragma.schema.TableInfoOperation
import im.dino.dbinspector.domain.pragma.schema.models.TableInfoColumns
import im.dino.dbinspector.domain.schema.table.ClearTableOperation
import im.dino.dbinspector.ui.content.shared.ContentViewModel

internal class TableViewModel(
    private val path: String,
    private val name: String
) : ContentViewModel(path) {

    override val nameOrdinal: Int = TableInfoColumns.NAME.ordinal

    override fun source() = TableDataSource(path, name, PAGE_SIZE)

    override fun info() = TableInfoOperation(name, PAGE_SIZE)

    override fun drop() = ClearTableOperation(name)
}