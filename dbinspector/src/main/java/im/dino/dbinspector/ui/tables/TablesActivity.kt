package im.dino.dbinspector.ui.tables

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityTablesBinding
import im.dino.dbinspector.domain.database.models.Database
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
                        toolbar.menu.findItem(R.id.refresh).isVisible = false
                        true
                    }
                    R.id.refresh -> {
                        adapter.submitData(lifecycle, PagingData.empty())
                        lifecycleScope.launch {
                            viewModel.query(database.absolutePath, (toolbar.menu.findItem(R.id.search).actionView as SearchView).query?.toString()).collectLatest {
                                adapter.submitData(it)
                            }
                        }
                        true
                    }
                    else -> false
                }
            }

            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            (toolbar.menu.findItem(R.id.search).actionView as SearchView).apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                setIconifiedByDefault(true)
                isSubmitButtonEnabled = false
                isQueryRefinementEnabled = true
                maxWidth = Integer.MAX_VALUE
                setOnCloseListener {
                    toolbar.menu.findItem(R.id.refresh).isVisible = true
                    false
                }
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        search(database, query)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        search(database, newText)
                        return true
                    }
                })
            }

            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                adapter.submitData(lifecycle, PagingData.empty())
                lifecycleScope.launch {
                    viewModel.query(
                        database.absolutePath,
                        (toolbar.menu.findItem(R.id.search).actionView as SearchView).query?.toString()

                    ).collectLatest {
                        adapter.submitData(it)
                    }
                }
            }

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
        lifecycleScope.launch {
            viewModel.query(database.absolutePath, query).collectLatest {
                (viewBinding.recyclerView.adapter as TablesAdapter).submitData(it)
            }
        }
    }
}