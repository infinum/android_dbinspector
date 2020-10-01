package im.dino.dbinspector.ui.content.trigger

import im.dino.dbinspector.R
import im.dino.dbinspector.domain.schema.models.SchemaType
import im.dino.dbinspector.ui.content.shared.ContentActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal class TriggerActivity : ContentActivity<TriggerViewModel>() {

    override val type: SchemaType = SchemaType.TRIGGER

    override val title: Int = R.string.dbinspector_trigger

    override val menu: Int = R.menu.dbinspector_trigger

    override val drop: Int = R.string.dbinspector_drop_trigger_confirm
}