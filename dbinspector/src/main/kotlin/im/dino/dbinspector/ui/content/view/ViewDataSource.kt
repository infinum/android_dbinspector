package im.dino.dbinspector.ui.content.view

import im.dino.dbinspector.domain.schema.view.ViewContentOperation
import im.dino.dbinspector.ui.content.shared.ContentDataSource

internal class ViewDataSource(
    path: String,
    private val name: String
) : ContentDataSource(path) {

    override fun operation() = lazy {
        ViewContentOperation(name)
    }
}