package im.dino.dbinspector.ui.content.trigger

import im.dino.dbinspector.domain.schema.trigger.DropTriggerOperation
import im.dino.dbinspector.domain.schema.trigger.TriggerInfoOperation
import im.dino.dbinspector.ui.content.shared.ContentViewModel

internal class TriggerViewModel(
    private val path: String,
    private val name: String
) : ContentViewModel(path) {

    override val nameOrdinal: Int = 0

    override fun source() = TriggerDataSource(path, name)

    override fun info() = TriggerInfoOperation()

    override fun drop() = DropTriggerOperation(name)
}