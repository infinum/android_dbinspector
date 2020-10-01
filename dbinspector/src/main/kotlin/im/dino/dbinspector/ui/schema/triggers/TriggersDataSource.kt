package im.dino.dbinspector.ui.schema.triggers

import im.dino.dbinspector.domain.schema.trigger.AllTriggersOperation
import im.dino.dbinspector.ui.schema.shared.SchemaDataSource

internal class TriggersDataSource(
    path: String,
    private val pageSize: Int,
    private val args: String?,
    empty: suspend (value: Boolean) -> Unit
) : SchemaDataSource(path, empty) {

    override fun source() = lazy { AllTriggersOperation(pageSize, args) }
}