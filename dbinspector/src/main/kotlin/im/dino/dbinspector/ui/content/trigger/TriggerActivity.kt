package im.dino.dbinspector.ui.content.trigger

import im.dino.dbinspector.R
import im.dino.dbinspector.ui.content.shared.ContentActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
internal class TriggerActivity : ContentActivity() {

    override val viewModel: TriggerViewModel by viewModel()

    override val title: Int = R.string.dbinspector_trigger

    override val menu: Int = R.menu.dbinspector_trigger

    override val drop: Int = R.string.dbinspector_drop_trigger_confirm
}
