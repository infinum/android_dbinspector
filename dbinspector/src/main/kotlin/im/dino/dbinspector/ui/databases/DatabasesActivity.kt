package im.dino.dbinspector.ui.databases

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityDatabasesBinding
import im.dino.dbinspector.domain.database.models.DatabaseDescriptor
import im.dino.dbinspector.extensions.scale
import im.dino.dbinspector.extensions.searchView
import im.dino.dbinspector.extensions.setup
import im.dino.dbinspector.ui.databases.DatabaseNavigator.Companion.REQUEST_CODE_IMPORT
import im.dino.dbinspector.ui.shared.base.BaseActivity
import im.dino.dbinspector.ui.shared.delegates.viewBinding
import im.dino.dbinspector.ui.shared.listeners.FabExtendingOnScrollListener
import im.dino.dbinspector.ui.shared.searchable.Searchable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
@FlowPreview
internal class DatabasesActivity : BaseActivity(), Searchable {

    override val binding by viewBinding(DbinspectorActivityDatabasesBinding::inflate)

    private val viewModel: DatabaseViewModel by viewModel()

    private val navigator = DatabaseNavigator(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()

        viewModel.databases.observeForever {
            showDatabases(it)
        }

        viewModel.browse()

        viewModel.observe {
            refreshDatabases()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMPORT) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.clipData?.let { clipData ->
                        viewModel.import((0 until clipData.itemCount).map { clipData.getItemAt(it).uri })
                    } ?: data?.data?.let {
                        viewModel.import(listOf(it))
                    } ?: Unit
                }
                Activity.RESULT_CANCELED -> Unit
                else -> Unit
            }
        }
    }

    override fun onSearchOpened() = Unit

    override fun search(query: String?) {
        with(binding) {
            (recyclerView.adapter as? DatabasesAdapter)?.filter?.filter(query)
        }
    }

    override fun searchQuery(): String? =
        binding.toolbar.menu.searchView?.query?.toString()

    override fun onSearchClosed() {
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
            addOnScrollListener(FabExtendingOnScrollListener(binding.importButton))
            layoutManager = LinearLayoutManager(
                this@DatabasesActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
        with(binding.importButton) {
            setOnClickListener { navigator.importDatabase() }
        }
        with(binding.emptyLayout) {
            retryButton.setOnClickListener {
                refreshDatabases()
            }
        }
    }

    private fun showDatabases(databases: List<DatabaseDescriptor>) {
        with(binding) {
            recyclerView.adapter = DatabasesAdapter(
                items = databases,
                onClick = { navigator.showSchema(it) },
                interactions = DatabaseInteractions(
                    onDelete = { removeDatabase(it) },
                    onEdit = { navigator.editDatabase(it) },
                    onCopy = { viewModel.copy(it) },
                    onShare = { navigator.shareDatabase(it) },
                ),
                onEmpty = {
                    emptyLayout.root.isVisible = it
                    swipeRefresh.isVisible = it.not()
                }
            )
            emptyLayout.root.isVisible = databases.isEmpty()
            swipeRefresh.isVisible = databases.isNotEmpty()
        }
    }

    private fun refreshDatabases() {
        viewModel.browse()
        search(searchQuery())
    }

    private fun removeDatabase(database: DatabaseDescriptor) =
        MaterialAlertDialogBuilder(this)
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
