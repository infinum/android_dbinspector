package im.dino.dbinspector.ui.schema

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivitySchemaBinding
import im.dino.dbinspector.ui.schema.tables.TablesViewModel
import im.dino.dbinspector.ui.shared.Constants

internal class SchemaActivity : AppCompatActivity() {

    private val viewModel by viewModels<TablesViewModel>()

    lateinit var binding: DbinspectorActivitySchemaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DbinspectorActivitySchemaBinding.inflate(layoutInflater)

        setContentView(binding.root)

        intent.extras?.let {
            val databaseName = it.getString(Constants.Keys.DATABASE_NAME)
            val databasePath = it.getString(Constants.Keys.DATABASE_PATH)
            if (databaseName.isNullOrBlank().not() && databasePath.isNullOrBlank().not()) {
                setupUi(databaseName!!, databasePath!!)
            } else {
                showError()
            }
        } ?: showError()
    }

    private fun setupUi(databaseName: String, databasePath: String) {
        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = databaseName
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.refresh -> {
                        refreshChildren()
                        true
                    }
                    else -> false
                }
            }

            tabLayout.setupWithViewPager(viewPager)
            viewPager.adapter = SchemaTypeAdapter(
                context = this@SchemaActivity,
                fragmentManager = supportFragmentManager,
                databasePath = databasePath,
                databaseName = databaseName
            )
        }
    }

    private fun showError() {
        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = "Error"

            // TODO: push or show error views or Fragment
        }
    }

    private fun refreshChildren() =
        supportFragmentManager
            .fragments
            .filterIsInstance<SwipeRefreshLayout.OnRefreshListener>()
            .forEach(SwipeRefreshLayout.OnRefreshListener::onRefresh)

    /*
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

     */
}