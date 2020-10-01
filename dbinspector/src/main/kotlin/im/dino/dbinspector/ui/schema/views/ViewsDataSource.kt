package im.dino.dbinspector.ui.schema.views

import im.dino.dbinspector.domain.schema.view.AllViewsOperation
import im.dino.dbinspector.ui.schema.shared.SchemaDataSource

internal class ViewsDataSource(
    path: String,
    private val pageSize: Int,
    private val args: String?,
    empty: suspend (value: Boolean) -> Unit
) : SchemaDataSource(path, empty) {

    override fun source() = lazy { AllViewsOperation(pageSize, args) }
}