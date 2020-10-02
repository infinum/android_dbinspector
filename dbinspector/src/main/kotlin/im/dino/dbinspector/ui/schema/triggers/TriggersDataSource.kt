package im.dino.dbinspector.ui.schema.triggers

import im.dino.dbinspector.domain.schema.trigger.AllTriggersOperation
import im.dino.dbinspector.ui.schema.shared.SchemaDataSource

internal class TriggersDataSource(
    path: String,
    pageSize: Int,
    argument: String?,
    onEmpty: suspend (value: Boolean) -> Unit
) : SchemaDataSource(path, onEmpty) {

    override val source = lazyOf(AllTriggersOperation(pageSize, argument))
}
