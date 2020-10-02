package im.dino.dbinspector.ui.content.view

import im.dino.dbinspector.domain.pragma.schema.TableInfoOperation
import im.dino.dbinspector.domain.pragma.schema.models.TableInfoColumns
import im.dino.dbinspector.domain.schema.view.DropViewOperation
import im.dino.dbinspector.ui.content.shared.ContentViewModel

internal class ViewViewModel(
    private val path: String,
    private val name: String
) : ContentViewModel(path) {

    override val nameOrdinal: Int = TableInfoColumns.NAME.ordinal

    override fun source() = ViewDataSource(path, name, PAGE_SIZE)

    override fun info() = TableInfoOperation(name, PAGE_SIZE)

    override fun drop() = DropViewOperation(name)
}
