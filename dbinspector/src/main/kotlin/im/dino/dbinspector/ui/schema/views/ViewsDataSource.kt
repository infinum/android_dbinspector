package im.dino.dbinspector.ui.schema.views

import im.dino.dbinspector.domain.schema.view.AllViewsOperation
import im.dino.dbinspector.ui.schema.shared.SchemaDataSource

internal class ViewsDataSource(
    path: String,
    pageSize: Int,
    args: String?,
    empty: suspend (value: Boolean) -> Unit
) : SchemaDataSource(path, empty) {

    override val source = lazyOf(AllViewsOperation(pageSize, args))
}
