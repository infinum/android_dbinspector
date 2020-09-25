package im.dino.dbinspector.domain.pragma

import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractTableOperation

internal class IndexesOperation(
    private val name: String,
    pageSize: Int
) : AbstractTableOperation<List<Row>>(pageSize) {

    override fun query(): String = String.format(FORMAT_PRAGMA_INDEXES, name)
}