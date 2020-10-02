package im.dino.dbinspector.domain.schema.view

import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractSchemaOperation

internal class DropViewOperation(
    private val name: String
) : AbstractSchemaOperation<List<Row>>(1) {

    override fun query(): String = String.format(FORMAT_DROP_VIEW, name)
}