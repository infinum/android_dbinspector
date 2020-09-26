package im.dino.dbinspector.domain.schema.trigger

import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractSchemaOperation

internal class TriggerContentOperation(
    private val name: String,
    private val pageSize: Int
) : AbstractSchemaOperation<List<Row>>(pageSize) {

    override fun query(): String = String.format(FORMAT_CONTENT_TRIGGER, name)

    override fun pageSize(): Int = pageSize
}