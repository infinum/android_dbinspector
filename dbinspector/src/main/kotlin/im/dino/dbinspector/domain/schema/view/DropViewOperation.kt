package im.dino.dbinspector.domain.schema.view

import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractSchemaOperation

internal class DropViewOperation(
    private val name: String,
    private val pageSize: Int
) : AbstractSchemaOperation<List<Row>>(pageSize) {

    override fun query(): String = String.format(FORMAT_DROP_VIEW, name)

    override fun pageSize(): Int = pageSize
}