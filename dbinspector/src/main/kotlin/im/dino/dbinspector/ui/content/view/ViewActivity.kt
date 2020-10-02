package im.dino.dbinspector.ui.content.view

import im.dino.dbinspector.R
import im.dino.dbinspector.domain.schema.models.SchemaType
import im.dino.dbinspector.ui.content.shared.ContentActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal class ViewActivity : ContentActivity<ViewViewModel>() {

    override val type: SchemaType = SchemaType.VIEW

    override val title: Int = R.string.dbinspector_view

    override val menu: Int = R.menu.dbinspector_view

    override val drop: Int = R.string.dbinspector_drop_view_confirm
}
