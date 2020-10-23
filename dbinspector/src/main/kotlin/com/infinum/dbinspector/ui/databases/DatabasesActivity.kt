package com.infinum.dbinspector.ui.databases

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivityDatabasesBinding
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.extensions.scale
import com.infinum.dbinspector.extensions.searchView
import com.infinum.dbinspector.extensions.setup
import com.infinum.dbinspector.ui.databases.edit.EditContract
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.listeners.FabExtendingOnScrollListener
import com.infinum.dbinspector.ui.shared.searchable.Searchable
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class DatabasesActivity : BaseActivity(), Searchable {

    override val binding by viewBinding(DbinspectorActivityDatabasesBinding::inflate)

    private val viewModel: DatabaseViewModel by viewModel()

    private val navigatorIntentFactory = NavigatorIntentFactory(this)

    private val editContract = registerForActivityResult(EditContract()) { shouldRefresh ->
        if (shouldRefresh) {
            refreshDatabases()
        }
    }

    private val importContract = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                result.data?.clipData?.let { clipData ->
                    viewModel.import((0 until clipData.itemCount).map { clipData.getItemAt(it).uri })
                } ?: result.data?.data?.let {
                    viewModel.import(listOf(it))
                } ?: Unit
            }
            Activity.RESULT_CANCELED -> Unit
            else -> Unit
        }
    }

    private val databasesAdapter: DatabasesAdapter = DatabasesAdapter(
        onClick = { navigatorIntentFactory.showSchema(it) },
        interactions = DatabaseInteractions(
            onDelete = { removeDatabase(it) },
            onEdit = {
                editContract.launch(
                    EditContract.Input(
                        absolutePath = it.absolutePath,
                        parentPath = it.parentPath,
                        name = it.name,
                        extension = it.extension
                    )
                )
            },
            onCopy = { viewModel.copy(it) },
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

        viewModel.databases.observeForever {
            showDatabases(it)
        }

        viewModel.browse()
    }

    override fun onSearchOpened() {
        binding.importButton.hide()
    }

    override fun search(query: String?) {
        viewModel.browse(query)
    }

    override fun searchQuery(): String? =
        binding.toolbar.menu.searchView?.query?.toString()

    override fun onSearchClosed() {
        binding.importButton.show()
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
            databasesAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            adapter = databasesAdapter

            addOnScrollListener(FabExtendingOnScrollListener(binding.importButton))
            layoutManager = LinearLayoutManager(
                this@DatabasesActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
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

    private fun removeDatabase(database: DatabaseDescriptor) =
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dbinspector_title_confirm)
            .setMessage(String.format(getString(R.string.dbinspector_delete_database_confirm), database.name))
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                viewModel.remove(database)
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
            .show()
}
