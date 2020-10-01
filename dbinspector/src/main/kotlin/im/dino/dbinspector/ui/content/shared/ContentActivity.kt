package im.dino.dbinspector.ui.content.shared

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityContentBinding
import im.dino.dbinspector.domain.schema.models.SchemaType
import im.dino.dbinspector.ui.content.table.TableViewModel
import im.dino.dbinspector.ui.content.trigger.TriggerViewModel
import im.dino.dbinspector.ui.content.view.ViewViewModel
import im.dino.dbinspector.ui.pragma.PragmaActivity
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.bus.EventBus
import im.dino.dbinspector.ui.shared.bus.models.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal abstract class ContentActivity<T : ContentViewModel> : AppCompatActivity() {

    lateinit var binding: DbinspectorActivityContentBinding

    private lateinit var viewModel: ContentViewModel

    abstract val type: SchemaType

    @get:StringRes
    abstract val title: Int

    @get:MenuRes
    abstract val menu: Int

    @get:StringRes
    abstract val drop: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DbinspectorActivityContentBinding.inflate(layoutInflater)

        setContentView(binding.root)

        intent.extras?.let {
            val databaseName = it.getString(Constants.Keys.DATABASE_NAME)
            val databasePath = it.getString(Constants.Keys.DATABASE_PATH)
            val tableName = it.getString(Constants.Keys.SCHEMA_NAME)
            if (databaseName.isNullOrBlank().not() && databasePath.isNullOrBlank().not() && tableName.isNullOrBlank().not()) {
                viewModel = resolveViewModel(databasePath!!, tableName!!)

                setupToolbar(databasePath, databaseName!!, tableName)
                setupSwipeRefresh()
                setupRecyclerView()

                viewModel.header { tableHeaders ->
                    binding.recyclerView.layoutManager = GridLayoutManager(this, tableHeaders.size)
                    binding.recyclerView.adapter = ContentAdapter(tableHeaders)

                    query()
                }
            } else {
                showError()
            }
        } ?: showError()
    }

    private inline fun <reified T : ViewModel> resolveViewModel(databasePath: String, tableName: String): T =
        ViewModelProvider(
            this,
            ContentViewModelFactory(
                type,
                databasePath,
                tableName
            )
        ).get(T::class.java)

    private fun setupToolbar(databasePath: String, databaseName: String, schemaName: String) =
        with(binding.toolbar) {
            setNavigationOnClickListener { finish() }
            title = getString(this@ContentActivity.title)
            subtitle = listOf(databaseName, schemaName).joinToString(" / ")
            menuInflater.inflate(this@ContentActivity.menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.clear -> {
                        promptDrop(schemaName)
                        true
                    }
                    R.id.drop -> {
                        promptDrop(schemaName)
                        true
                    }
                    R.id.pragma -> {
                        pragma(databaseName, databasePath, schemaName)
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
        }

    private fun showError() {

    }

    private fun pragma(databaseName: String?, databasePath: String?, schemaName: String) {
        startActivity(
            Intent(this, PragmaActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Constants.Keys.SCHEMA_NAME, schemaName)
                }
        )
    }

    private fun promptDrop(name: String) =
        MaterialAlertDialogBuilder(this)
            .setMessage(String.format(getString(drop), name))
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

    private fun drop() =
        viewModel.drop() {
            when (viewModel) {
                is TableViewModel -> clearTable(it)
                is TriggerViewModel -> dropTrigger()
                is ViewViewModel -> dropView()
            }
        }

    private suspend fun clearTable(data: PagingData<String>) =
        (binding.recyclerView.adapter as? ContentAdapter)?.submitData(data)

    private suspend fun dropTrigger() {
        EventBus.send(Event.RefreshTriggers())
        finish()
    }

    private suspend fun dropView() {
        EventBus.send(Event.RefreshViews())
        finish()
    }
}