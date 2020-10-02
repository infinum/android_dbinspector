package im.dino.dbinspector.ui.content.table

import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.schema.table.TableContentOperation
import im.dino.dbinspector.domain.shared.AbstractSchemaOperation
import im.dino.dbinspector.ui.content.shared.ContentDataSource
import im.dino.dbinspector.ui.shared.base.BaseViewModel.Companion.PAGE_SIZE

internal class TableDataSource(
    path: String,
    name: String
) : ContentDataSource(path) {

    override val operation: Lazy<AbstractSchemaOperation<List<Row>>> =
        lazyOf(TableContentOperation(name, PAGE_SIZE))
}