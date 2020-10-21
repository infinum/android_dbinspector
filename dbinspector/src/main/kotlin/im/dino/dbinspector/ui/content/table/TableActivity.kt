package im.dino.dbinspector.ui.content.table

import im.dino.dbinspector.R
import im.dino.dbinspector.ui.content.shared.ContentActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TableActivity : ContentActivity() {

    override val viewModel: TableViewModel by viewModel()

    override val title: Int = R.string.dbinspector_table

    override val menu: Int = R.menu.dbinspector_table

    override val drop: Int = R.string.dbinspector_clear_table_confirm
}
