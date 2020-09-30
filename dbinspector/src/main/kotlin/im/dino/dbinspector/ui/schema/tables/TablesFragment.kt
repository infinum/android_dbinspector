package im.dino.dbinspector.ui.schema.tables

import androidx.fragment.app.viewModels
import im.dino.dbinspector.ui.schema.shared.SchemaFragment
import im.dino.dbinspector.ui.table.TableActivity

internal class TablesFragment : SchemaFragment() {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): TablesFragment =
            TablesFragment().apply {
                arguments = bundle(databasePath, databaseName)
            }
    }

    override val viewModel by viewModels<TablesViewModel>()

    override fun itemClass() = TableActivity::class.java
}