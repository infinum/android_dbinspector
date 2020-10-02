package im.dino.dbinspector.domain.schema.trigger

import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractSchemaOperation

internal class DropTriggerOperation(
    private val name: String
) : AbstractSchemaOperation<List<Row>>(1) {

    override fun query(): String = String.format(FORMAT_DROP_TRIGGER, name)
}