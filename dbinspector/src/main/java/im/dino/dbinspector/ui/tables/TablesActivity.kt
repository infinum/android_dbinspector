package im.dino.dbinspector.ui.tables

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import im.dino.dbinspector.databinding.DbinspectorActivityTablesBinding
import im.dino.dbinspector.domain.database.models.Database
import im.dino.dbinspector.domain.table.models.Table
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.tables.content.ContentActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TablesActivity : AppCompatActivity() {

    private val viewModel by viewModels<TablesViewModel>()

    lateinit var viewBinding: DbinspectorActivityTablesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DbinspectorActivityTablesBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        intent.extras?.getParcelable<Database>(Constants.Keys.DATABASE)?.let {
            setupUi(it)
        } ?: showError()
    }

    private fun setupUi(database: Database) {
        with(viewBinding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = database.name

            val adapter = TablesAdapter(
                onClick = {
                    showTableContent(database.name, database.absolutePath, it)
                }
            )
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayout.VERTICAL))
            recyclerView.adapter = adapter

            lifecycleScope.launch {
                viewModel.query(database.absolutePath).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun showError() {
        // TODO
    }

    private fun showTableContent(databaseName: String, databasePath: String, table: Table) {
        startActivity(
            Intent(this, ContentActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Constants.Keys.TABLE, table)
                }
        )
    }
}