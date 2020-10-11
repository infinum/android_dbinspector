package im.dino.dbinspector.ui.content.view

import im.dino.dbinspector.R
import im.dino.dbinspector.ui.content.shared.ContentActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
internal class ViewActivity : ContentActivity() {

    override val viewModel: ViewViewModel by viewModel()

    override val title: Int = R.string.dbinspector_view

    override val menu: Int = R.menu.dbinspector_view

    override val drop: Int = R.string.dbinspector_drop_view_confirm
}
