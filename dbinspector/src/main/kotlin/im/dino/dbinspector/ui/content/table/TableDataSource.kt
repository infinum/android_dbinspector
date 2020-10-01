package im.dino.dbinspector.ui.content.table

import im.dino.dbinspector.domain.schema.table.TableContentOperation
import im.dino.dbinspector.ui.content.shared.ContentDataSource
import im.dino.dbinspector.ui.shared.base.BaseViewModel.Companion.PAGE_SIZE

internal class TableDataSource(
    path: String,
    private val name: String
) : ContentDataSource(path) {

    override fun operation() = lazy {
        TableContentOperation(name, PAGE_SIZE)
    }
}