package im.dino.dbinspector.ui.schema

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivitySchemaBinding
import im.dino.dbinspector.extensions.searchView
import im.dino.dbinspector.extensions.setup
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.Searchable

internal class SchemaActivity : AppCompatActivity(), Searchable {

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

    override fun onSearchOpened() {
        binding.toolbar.menu.findItem(R.id.refresh).isVisible = false
    }

    override fun search(query: String?) =
        supportFragmentManager
            .fragments
            .filterIsInstance<Searchable>()
            .forEach { it.search(query) }

    override fun searchQuery(): String? =
        binding.toolbar.menu.searchView?.query?.toString()

    override fun onSearchClosed() {
        binding.toolbar.menu.findItem(R.id.refresh).isVisible = true
    }

    private fun setupUi(databaseName: String, databasePath: String) {
        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = databaseName
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search -> {
                        onSearchOpened()
                        true
                    }
                    R.id.refresh -> {
                        refreshChildren()
                        true
                    }
                    else -> false
                }
            }

            toolbar.menu.searchView?.setup(
                onSearchClosed = { onSearchClosed() },
                onQueryTextChanged = { search(it) }
            )

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

            // TODO: push or show error views or Fragment
        }
    }

    private fun refreshChildren() =
        supportFragmentManager
            .fragments
            .filterIsInstance<SwipeRefreshLayout.OnRefreshListener>()
            .forEach(SwipeRefreshLayout.OnRefreshListener::onRefresh)
}