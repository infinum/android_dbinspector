package im.dino.dbinspector.ui.tables

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityTablesBinding
import im.dino.dbinspector.domain.database.models.Database
import im.dino.dbinspector.extensions.searchView
import im.dino.dbinspector.extensions.setup
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.Searchable
import im.dino.dbinspector.ui.tables.content.ContentActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TablesActivity : AppCompatActivity(), Searchable {

    private val viewModel by viewModels<TablesViewModel>()

    lateinit var binding: DbinspectorActivityTablesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DbinspectorActivityTablesBinding.inflate(layoutInflater)

        setContentView(binding.root)

        intent.extras?.getParcelable<Database>(Constants.Keys.DATABASE)?.let {
            setupUi(it)
        } ?: showError()
    }

    override fun onSearchOpened() {
        binding.toolbar.menu.findItem(R.id.refresh).isVisible = false
    }

    override fun onSearchClosed() {
        binding.toolbar.menu.findItem(R.id.refresh).isVisible = true
    }

    private fun setupUi(database: Database) {
        with(binding) {
            val adapter = TablesAdapter(
                onClick = {
                    showTableContent(database.name, database.absolutePath, it)
                }
            )

            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = database.name
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search -> {
                        onSearchOpened()
                        true
                    }
                    R.id.refresh -> {
                        adapter.submitData(lifecycle, PagingData.empty())
                        viewModel.query(
                            lifecycleScope,
                            database.absolutePath,
                            toolbar.menu.searchView?.query?.toString()
                        ) {
                            adapter.submitData(it)
                        }
                        true
                    }
                    else -> false
                }
            }

            toolbar.menu.searchView?.setup(
                onSearchClosed = { onSearchClosed() },
                onQueryTextChanged = { search(database, it) }
            )

            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                adapter.submitData(lifecycle, PagingData.empty())
                viewModel.query(
                    lifecycleScope,
                    database.absolutePath,
                    toolbar.menu.searchView?.query?.toString()
                ) {
                    adapter.submitData(it)
                }
            }

            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayout.VERTICAL))
            recyclerView.adapter = adapter

            viewModel.query(lifecycleScope, database.absolutePath) {
                adapter.submitData(it)
            }
        }
    }

    private fun showError() {
        // TODO
    }

    private fun showTableContent(databaseName: String, databasePath: String, tableName: String) {
        startActivity(
            Intent(this, ContentActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Constants.Keys.TABLE_NAME, tableName)
                }
        )
    }

    private fun search(database: Database, query: String?) {
        viewModel.query(lifecycleScope, database.absolutePath, query) {
            (binding.recyclerView.adapter as TablesAdapter).submitData(it)
        }
    }
}