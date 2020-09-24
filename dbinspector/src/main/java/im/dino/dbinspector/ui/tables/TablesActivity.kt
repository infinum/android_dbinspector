package im.dino.dbinspector.ui.tables

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

class TablesActivity : AppCompatActivity(), Searchable {

    private val viewModel by viewModels<TablesViewModel>()

    lateinit var binding: DbinspectorActivityTablesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DbinspectorActivityTablesBinding.inflate(layoutInflater)

        setContentView(binding.root)

        intent.extras?.getParcelable<Database>(Constants.Keys.DATABASE)?.let {
            setupToolbar(it)
            setupSwipeRefresh(it)
            setupRecyclerView(it)
        }
    }

    override fun onSearchOpened() {
        binding.toolbar.menu.findItem(R.id.refresh).isVisible = false
    }

    override fun onSearchClosed() {
        binding.toolbar.menu.findItem(R.id.refresh).isVisible = true
    }

    private fun setupToolbar(database: Database) =
        with(binding.toolbar) {
            setNavigationOnClickListener { finish() }
            subtitle = database.name
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search -> {
                        onSearchOpened()
                        true
                    }
                    R.id.refresh -> {
                        (binding.recyclerView.adapter as? TablesAdapter)?.submitData(lifecycle, PagingData.empty())
                        query(database.absolutePath)
                        true
                    }
                    else -> false
                }
            }

            menu.searchView?.setup(
                onSearchClosed = { onSearchClosed() },
                onQueryTextChanged = { search(database, it) }
            )
        }

    private fun setupSwipeRefresh(database: Database) =
        with(binding.swipeRefresh) {
            setOnRefreshListener {
                isRefreshing = false

                (binding.recyclerView.adapter as? TablesAdapter)?.submitData(lifecycle, PagingData.empty())

                query(database.absolutePath)
            }
        }

    private fun setupRecyclerView(database: Database) =
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            adapter = TablesAdapter(
                onClick = {
                    showTableContent(database.name, database.absolutePath, it)
                }
            )

            query(database.absolutePath)
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

    private fun search(database: Database, query: String?) =
        viewModel.query(database.absolutePath, query) {
            (binding.recyclerView.adapter as? TablesAdapter)?.submitData(it)
        }

    private fun query(databasePath: String) {
        with(binding) {
            viewModel.query(
                databasePath,
                toolbar.menu.searchView?.query?.toString()
            ) {
                (recyclerView.adapter as? TablesAdapter)?.submitData(it)
            }
        }
    }
}