package im.dino.dbinspector.ui.schema.views

import androidx.fragment.app.viewModels
import im.dino.dbinspector.ui.schema.shared.SchemaFragment
import im.dino.dbinspector.ui.content.view.ViewActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal class ViewsFragment : SchemaFragment() {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): ViewsFragment =
            ViewsFragment().apply {
                arguments = bundle(databasePath, databaseName)
            }
    }

    override val viewModel by viewModels<ViewsViewModel>()

    override fun childView() = ViewActivity::class.java
}