package im.dino.dbinspector.ui.schema

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivitySchemaBinding
import im.dino.dbinspector.extensions.searchView
import im.dino.dbinspector.extensions.setup
import im.dino.dbinspector.ui.schema.shared.SchemaTypeAdapter
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.base.BaseActivity
import im.dino.dbinspector.ui.shared.base.searchable.Searchable
import im.dino.dbinspector.ui.shared.delegates.viewBinding

internal class SchemaActivity : BaseActivity(), Searchable {

    override val binding by viewBinding(DbinspectorActivitySchemaBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        }
    }

    private fun refreshChildren() =
        supportFragmentManager
            .fragments
            .filterIsInstance<SwipeRefreshLayout.OnRefreshListener>()
            .forEach(SwipeRefreshLayout.OnRefreshListener::onRefresh)
}
