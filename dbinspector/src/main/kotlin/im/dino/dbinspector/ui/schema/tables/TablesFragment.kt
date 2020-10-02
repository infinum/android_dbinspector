package im.dino.dbinspector.ui.schema.tables

import androidx.fragment.app.viewModels
import im.dino.dbinspector.ui.schema.shared.SchemaFragment
import im.dino.dbinspector.ui.content.table.TableActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal class TablesFragment : SchemaFragment() {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): TablesFragment =
            TablesFragment().apply {
                arguments = bundle(databasePath, databaseName)
            }
    }

    override val viewModel by viewModels<TablesViewModel>()

    override fun childView() = TableActivity::class.java
}