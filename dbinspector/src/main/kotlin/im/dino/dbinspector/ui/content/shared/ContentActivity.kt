package im.dino.dbinspector.ui.content.shared

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityContentBinding
import im.dino.dbinspector.ui.content.table.TableViewModel
import im.dino.dbinspector.ui.content.trigger.TriggerViewModel
import im.dino.dbinspector.ui.content.view.ViewViewModel
import im.dino.dbinspector.ui.pragma.PragmaActivity
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.base.BaseActivity
import im.dino.dbinspector.ui.shared.delegates.viewBinding
import im.dino.dbinspector.ui.shared.headers.HeaderAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal abstract class ContentActivity : BaseActivity() {

    override val binding by viewBinding(DbinspectorActivityContentBinding::inflate)

    abstract val viewModel: ContentViewModel

    @get:StringRes
    abstract val title: Int

    @get:MenuRes
    abstract val menu: Int

    @get:StringRes
    abstract val drop: Int

    private lateinit var contentAdapter: ContentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            val databaseName = it.getString(Constants.Keys.DATABASE_NAME)
            val databasePath = it.getString(Constants.Keys.DATABASE_PATH)
            val schemaName = it.getString(Constants.Keys.SCHEMA_NAME)
            if (
                databaseName.isNullOrBlank().not() &&
                databasePath.isNullOrBlank().not() &&
                schemaName.isNullOrBlank().not()
            ) {
                viewModel.databasePath = databasePath!!

                viewModel.open(lifecycleScope)

                setupUi(databasePath, databaseName!!, schemaName!!)

                viewModel.header(schemaName) { tableHeaders ->
                    val headerAdapter = HeaderAdapter(tableHeaders)
                    contentAdapter = ContentAdapter(tableHeaders.size)

                    with(binding) {
                        contentAdapter.addLoadStateListener { loadState ->
                            if (loadState.prepend.endOfPaginationReached) {
                                swipeRefresh.isRefreshing = loadState.refresh !is LoadState.NotLoading
                            }
                        }

                        swipeRefresh.setOnRefreshListener {
                            contentAdapter.refresh()
                        }

                        recyclerView.layoutManager = GridLayoutManager(
                            this@ContentActivity,
                            tableHeaders.size,
                            RecyclerView.VERTICAL,
                            false
                        )

                        ContextCompat.getDrawable(
                            recyclerView.context,
                            R.drawable.dbinspector_divider_vertical
                        )?.let { drawable ->
                            val verticalDecorator = DividerItemDecoration(
                                recyclerView.context,
                                DividerItemDecoration.VERTICAL
                            )
                            verticalDecorator.setDrawable(drawable)
                            recyclerView.addItemDecoration(verticalDecorator)
                        }
                        ContextCompat.getDrawable(
                            recyclerView.context,
                            R.drawable.dbinspector_divider_horizontal
                        )?.let { drawable ->
                            val horizontalDecorator = DividerItemDecoration(
                                recyclerView.context,
                                DividerItemDecoration.HORIZONTAL
                            )
                            horizontalDecorator.setDrawable(drawable)
                            recyclerView.addItemDecoration(horizontalDecorator)
                        }

                        recyclerView.adapter = ConcatAdapter(headerAdapter, contentAdapter)
                    }

                    query(schemaName)
                }
            } else {
                showError()
            }
        } ?: showError()
    }

    override fun onDestroy() {
        viewModel.close(lifecycleScope)
        super.onDestroy()
    }

    private fun setupUi(databasePath: String, databaseName: String, schemaName: String) {
        with(binding.toolbar) {
            setNavigationOnClickListener { finish() }
            title = getString(this@ContentActivity.title)
            subtitle = listOf(databaseName, schemaName).joinToString(" / ")
            menuInflater.inflate(this@ContentActivity.menu, menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.clear -> {
                        drop(schemaName)
                        true
                    }
                    R.id.drop -> {
                        drop(schemaName)
                        true
                    }
                    R.id.pragma -> {
                        pragma(databaseName, databasePath, schemaName)
                        true
                    }
                    else -> false
                }
            }
        }
        with(binding.recyclerView) {
            updateLayoutParams {
                minimumWidth = resources.displayMetrics.widthPixels
            }
        }
    }

    private fun showError() {
        println("Some error")
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

    private fun drop(name: String) =
        MaterialAlertDialogBuilder(this)
            .setMessage(String.format(getString(drop), name))
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                viewModel.drop(name) {
                    when (viewModel) {
                        is TableViewModel -> clearTable()
                        is TriggerViewModel -> dropTrigger()
                        is ViewViewModel -> dropView()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
            .show()

    private fun query(name: String) =
        viewModel.query(name) {
            contentAdapter.submitData(it)
        }

    private fun clearTable() =
        contentAdapter.refresh()

    private fun dropTrigger() {
        setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(Constants.Keys.SHOULD_REFRESH, true)
            }
        )
        finish()
    }

    private fun dropView() {
        setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(Constants.Keys.SHOULD_REFRESH, true)
            }
        )
        finish()
    }
}