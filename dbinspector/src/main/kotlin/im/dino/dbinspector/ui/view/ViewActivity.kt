package im.dino.dbinspector.ui.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityViewBinding
import im.dino.dbinspector.ui.pragma.schema.PragmaActivity
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.bus.EventBus
import im.dino.dbinspector.ui.shared.bus.models.Event
import im.dino.dbinspector.ui.shared.content.ContentAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal class ViewActivity : AppCompatActivity() {

    lateinit var binding: DbinspectorActivityViewBinding

    private lateinit var viewModel: ViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DbinspectorActivityViewBinding.inflate(layoutInflater)

        setContentView(binding.root)

        intent.extras?.let {
            val databaseName = it.getString(Constants.Keys.DATABASE_NAME)
            val databasePath = it.getString(Constants.Keys.DATABASE_PATH)
            val viewName = it.getString(Constants.Keys.VIEW_NAME)
            if (databaseName.isNullOrBlank().not() && databasePath.isNullOrBlank().not() && viewName.isNullOrBlank().not()) {
                viewModel = ViewModelProvider(
                    this,
                    ViewViewModelFactory(databasePath!!, viewName!!)
                ).get(ViewViewModel::class.java)

                setupToolbar(databasePath, databaseName!!, viewName)
                setupSwipeRefresh()
                setupRecyclerView()
            } else {
                showError()
            }
        } ?: showError()
    }

    private fun setupToolbar(databasePath: String, databaseName: String, viewName: String) =
        with(binding.toolbar) {
            setNavigationOnClickListener { finish() }
            subtitle = listOf(databaseName, viewName).joinToString(" / ")
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.drop -> {
                        dropView(viewName)
                        true
                    }
                    R.id.pragma -> {
                        showPragma(databaseName, databasePath, viewName)
                        true
                    }
                    R.id.refresh -> {
                        (binding.recyclerView.adapter as? ContentAdapter)?.submitData(lifecycle, PagingData.empty())
                        query()
                        true
                    }
                    else -> false
                }
            }
        }

    private fun setupSwipeRefresh() =
        with(binding.swipeRefresh) {
            setOnRefreshListener {
                isRefreshing = false

                (binding.recyclerView.adapter as? ContentAdapter)?.let { adapter ->
                    adapter.submitData(lifecycle, PagingData.empty())
                    query()
                }
            }
        }

    private fun setupRecyclerView() =
        with(binding.recyclerView) {
            updateLayoutParams {
                minimumWidth = resources.displayMetrics.widthPixels
            }

            viewModel.header { tableHeaders ->
                layoutManager = GridLayoutManager(context, tableHeaders.size)
                adapter = ContentAdapter(tableHeaders)

                query()
            }
        }

    private fun showError() {

    }

    private fun showPragma(databaseName: String?, databasePath: String?, schemaName: String) {
        startActivity(
            Intent(this, PragmaActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Constants.Keys.SCHEMA_NAME, schemaName)
                }
        )
    }

    private fun dropView(name: String) =
        MaterialAlertDialogBuilder(this)
            .setMessage(String.format(getString(R.string.dbinspector_drop_view_confirm), name))
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                drop()
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
            .show()

    private fun query() =
        viewModel.query() {
            (binding.recyclerView.adapter as? ContentAdapter)?.submitData(it)
        }

    @ExperimentalCoroutinesApi
    private fun drop() =
        viewModel.drop() {
            EventBus.send(Event.RefreshViews())
            finish()
        }
}