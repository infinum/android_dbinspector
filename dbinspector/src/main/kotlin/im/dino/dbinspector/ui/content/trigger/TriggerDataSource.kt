package im.dino.dbinspector.ui.content.trigger

import im.dino.dbinspector.domain.schema.trigger.TriggerContentOperation
import im.dino.dbinspector.ui.content.shared.ContentDataSource

internal class TriggerDataSource(
    path: String,
    name: String
) : ContentDataSource(path) {

    override val operation = lazyOf(TriggerContentOperation(name))
}