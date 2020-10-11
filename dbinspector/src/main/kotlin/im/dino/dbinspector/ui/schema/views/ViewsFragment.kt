package im.dino.dbinspector.ui.schema.views

import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.content.view.ViewActivity
import im.dino.dbinspector.ui.schema.shared.SchemaFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
internal class ViewsFragment : SchemaFragment() {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): ViewsFragment =
            ViewsFragment().apply {
                arguments = bundle(databasePath, databaseName)
            }
    }

    override var statement: String = Statements.Schema.views()

    override val viewModel: ViewsViewModel by viewModel()

    override fun childView() = ViewActivity::class.java
}
