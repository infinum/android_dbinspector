package im.dino.dbinspector.domain.pragma

import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractTableOperation

internal class TableInfoOperation(
    private val name: String,
    pageSize: Int
) : AbstractTableOperation<List<Row>>(pageSize) {

    override fun query(): String = String.format(FORMAT_TABLE_INFO, name)
}