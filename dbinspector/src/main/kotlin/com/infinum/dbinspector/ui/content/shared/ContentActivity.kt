package com.infinum.dbinspector.ui.content.shared

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
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivityContentBinding
import com.infinum.dbinspector.domain.shared.models.Direction
import com.infinum.dbinspector.ui.content.table.TableViewModel
import com.infinum.dbinspector.ui.content.trigger.TriggerViewModel
import com.infinum.dbinspector.ui.content.view.ViewViewModel
import com.infinum.dbinspector.ui.pragma.PragmaActivity
import com.infinum.dbinspector.ui.shared.Constants
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.headers.HeaderAdapter

internal abstract class ContentActivity : BaseActivity() {

    override val binding by viewBinding(DbinspectorActivityContentBinding::inflate)

    abstract val viewModel: ContentViewModel

    @get:StringRes
    abstract val title: Int

    @get:MenuRes
    abstract val menu: Int

    @get:StringRes
    abstract val drop: Int

    private lateinit var headerAdapter: HeaderAdapter

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
                    headerAdapter = HeaderAdapter(tableHeaders, true) { header ->
                        query(schemaName, header.name, header.direction)
                        headerAdapter.updateHeader(header)
                    }

                    contentAdapter = ContentAdapter(tableHeaders.size)
                    contentAdapter.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

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
            subtitle = listOf(databaseName, schemaName).joinToString(" → ")
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
            ContextCompat.getDrawable(
                context,
                R.drawable.dbinspector_divider_vertical
            )?.let { drawable ->
                val verticalDecorator = DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
                verticalDecorator.setDrawable(drawable)
                addItemDecoration(verticalDecorator)
            }
            ContextCompat.getDrawable(
                context,
                R.drawable.dbinspector_divider_horizontal
            )?.let { drawable ->
                val horizontalDecorator = DividerItemDecoration(
                    context,
                    DividerItemDecoration.HORIZONTAL
                )
                horizontalDecorator.setDrawable(drawable)
                addItemDecoration(horizontalDecorator)
            }
            updateLayoutParams {
                minimumWidth = resources.displayMetrics.widthPixels
            }
        }
    }

    private fun showError() =
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(R.string.dbinspector_title_error)
            .setMessage(R.string.dbinspector_error_parameters)
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                finish()
            }
            .create()
            .show()

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
            .setTitle(R.string.dbinspector_title_info)
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

    private fun query(
        name: String,
        orderBy: String? = null,
        direction: Direction = Direction.ASCENDING
    ) =
        viewModel.query(name, orderBy, direction) {
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
