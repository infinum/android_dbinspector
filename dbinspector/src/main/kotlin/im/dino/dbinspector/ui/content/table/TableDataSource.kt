package im.dino.dbinspector.ui.content.table

import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.schema.table.TableContentOperation
import im.dino.dbinspector.domain.shared.AbstractSchemaOperation
import im.dino.dbinspector.ui.content.shared.ContentDataSource

internal class TableDataSource(
    path: String,
    name: String,
    pageSize: Int
) : ContentDataSource(path) {

    override val operation: Lazy<AbstractSchemaOperation<List<Row>>> =
        lazyOf(TableContentOperation(name, pageSize))
}