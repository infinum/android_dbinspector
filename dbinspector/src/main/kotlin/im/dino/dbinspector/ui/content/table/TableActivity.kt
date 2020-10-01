package im.dino.dbinspector.ui.content.table

import im.dino.dbinspector.R
import im.dino.dbinspector.domain.schema.models.SchemaType
import im.dino.dbinspector.ui.content.shared.ContentActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal class TableActivity : ContentActivity<TableViewModel>() {

    override val type: SchemaType = SchemaType.TABLE

    override val title: Int = R.string.dbinspector_table

    override val menu: Int = R.menu.dbinspector_table

    override val drop: Int = R.string.dbinspector_clear_table_confirm
}