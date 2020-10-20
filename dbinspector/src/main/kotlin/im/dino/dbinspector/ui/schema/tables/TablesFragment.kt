package im.dino.dbinspector.ui.schema.tables

import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.content.table.TableActivity
import im.dino.dbinspector.ui.schema.shared.SchemaFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
internal class TablesFragment : SchemaFragment() {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): TablesFragment =
            TablesFragment().apply {
                arguments = bundle(databasePath, databaseName)
            }
    }

    override var statement: String = Statements.Schema.tables()

    override val viewModel: TablesViewModel by viewModel()

    override fun childView() = TableActivity::class.java
}