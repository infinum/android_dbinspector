package im.dino.dbinspector.ui.content.view

import im.dino.dbinspector.domain.schema.view.ViewContentOperation
import im.dino.dbinspector.ui.content.shared.ContentDataSource

internal class ViewDataSource(
    path: String,
    name: String,
    pageSize: Int
) : ContentDataSource(path) {

    override val operation = lazyOf(ViewContentOperation(name, pageSize))
}