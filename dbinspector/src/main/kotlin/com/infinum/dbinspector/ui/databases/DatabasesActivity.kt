package com.infinum.dbinspector.ui.databases

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivityDatabasesBinding
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.extensions.scale
import com.infinum.dbinspector.extensions.searchView
import com.infinum.dbinspector.extensions.setup
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.REMOVE_DATABASE
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.RENAME_DATABASE
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.SHOULD_REFRESH
import com.infinum.dbinspector.ui.databases.remove.RemoveDatabaseDialog
import com.infinum.dbinspector.ui.databases.rename.RenameDatabaseDialog
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.edgefactories.bounce.BounceEdgeEffectFactory
import com.infinum.dbinspector.ui.shared.listeners.FabExtendingOnScrollListener
import com.infinum.dbinspector.ui.shared.searchable.Searchable
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
internal class DatabasesActivity : BaseActivity<DatabaseState, Any>(), Searchable {

    override val binding by viewBinding(DbinspectorActivityDatabasesBinding::inflate)

    override val viewModel: DatabaseViewModel by viewModel()

    private val navigatorIntentFactory = NavigatorIntentFactory(this)

    private val importContract = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                result.data?.clipData?.let { clipData ->
                    viewModel.import(this, (0 until clipData.itemCount).map { clipData.getItemAt(it).uri })
                } ?: result.data?.data?.let {
                    viewModel.import(this, listOf(it))
                } ?: Unit
            }
            Activity.RESULT_CANCELED -> Unit
            else -> Unit
        }
    }

    private val databasesAdapter: DatabasesAdapter = DatabasesAdapter(
        onClick = { navigatorIntentFactory.showSchema(it) },
        interactions = DatabaseInteractions(
            onDelete = {
                RemoveDatabaseDialog
                    .withDatabaseDescriptor(it)
                    .show(supportFragmentManager, null)
            },
            onRename = {
                RenameDatabaseDialog
                    .withDatabaseDescriptor(it)
                    .show(supportFragmentManager, null)
            },
            onCopy = { viewModel.copy(this, it) },
            onShare = { navigatorIntentFactory.showShare(it) },
        ),
        onEmpty = {
            binding.emptyLayout.root.isVisible = it
            binding.swipeRefresh.isVisible = it.not()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()

        supportFragmentManager.setFragmentResultListener(REMOVE_DATABASE, this) { _, bundle ->
            if (bundle.getBoolean(SHOULD_REFRESH, false)) {
                refreshDatabases()
            }
        }
        supportFragmentManager.setFragmentResultListener(RENAME_DATABASE, this) { _, bundle ->
            if (bundle.getBoolean(SHOULD_REFRESH, false)) {
                refreshDatabases()
            }
        }

        viewModel.browse(this)
    }

    override fun onState(state: DatabaseState) {
        when (state) {
            is DatabaseState.Databases -> showDatabases(state.databases)
        }
    }

    override fun onEvent(event: Any) = Unit

    override fun onSearchOpened() {
        with(binding) {
            importButton.hide()
            toolbar.menu.findItem(R.id.settings).isVisible = false
        }
    }

    override fun search(query: String?) {
        viewModel.browse(this, query)
    }

    override fun searchQuery(): String? =
        binding.toolbar.menu.searchView?.query?.toString()

    override fun onSearchClosed() {
        with(binding) {
            toolbar.menu.findItem(R.id.settings).isVisible = true
            importButton.show()
        }
        refreshDatabases()
    }

    private fun setupUi() {
        with(binding.toolbar) {
            navigationIcon = applicationInfo
                .loadIcon(packageManager)
                .scale(this@DatabasesActivity, R.dimen.dbinspector_app_icon_size, R.dimen.dbinspector_app_icon_size)
            setNavigationOnClickListener { finish() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search -> {
                        onSearchOpened()
                        true
                    }
                    R.id.settings -> {
                        navigatorIntentFactory.showSettings()
                        true
                    }
                    else -> false
                }
            }
            menu.searchView?.setup(
                hint = getString(R.string.dbinspector_search_by_name),
                onSearchClosed = { onSearchClosed() },
                onQueryTextChanged = { search(it) }
            )
        }
        with(binding.swipeRefresh) {
            setOnRefreshListener {
                isRefreshing = false
                refreshDatabases()
            }
        }
        with(binding.recyclerView) {
            adapter = databasesAdapter
            edgeEffectFactory = BounceEdgeEffectFactory()
            addOnScrollListener(FabExtendingOnScrollListener(binding.importButton))

            layoutManager = if (resources.getBoolean(R.bool.dbinspector_is_tablet)) {
                GridLayoutManager(
                    this@DatabasesActivity,
                    resources.getInteger(R.integer.dbinspector_span_count)
                )
            } else {
                LinearLayoutManager(
                    this@DatabasesActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
        with(binding.importButton) {
            setOnClickListener {
                importContract.launch(navigatorIntentFactory.showImportChooser())
            }
        }
        with(binding.emptyLayout) {
            retryButton.setOnClickListener {
                refreshDatabases()
            }
        }
    }

    private fun showDatabases(databases: List<DatabaseDescriptor>) {
        databasesAdapter.submitList(databases)
    }

    private fun refreshDatabases() {
        search(searchQuery())
    }
}
