package com.infinum.dbinspector.ui.content.shared

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivityContentBinding
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.ui.extensions.setupAsTable
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.DROP_CONTENT
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.DROP_NAME
import com.infinum.dbinspector.ui.content.shared.drop.DropContentDialog
import com.infinum.dbinspector.ui.content.shared.preview.PreviewContentDialog
import com.infinum.dbinspector.ui.content.table.TableViewModel
import com.infinum.dbinspector.ui.content.trigger.TriggerViewModel
import com.infinum.dbinspector.ui.content.view.ViewViewModel
import com.infinum.dbinspector.ui.pragma.PragmaActivity
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.base.lifecycle.LifecycleConnection
import com.infinum.dbinspector.ui.shared.contracts.EditContract
import com.infinum.dbinspector.ui.shared.delegates.lifecycleConnection
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.edgefactories.bounce.BounceEdgeEffectFactory
import com.infinum.dbinspector.ui.shared.headers.HeaderAdapter
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
internal abstract class ContentActivity : BaseActivity<ContentState, ContentEvent>() {

    override val binding by viewBinding(DbinspectorActivityContentBinding::inflate)

    abstract override val viewModel: ContentViewModel

    private val connection: LifecycleConnection by lifecycleConnection()

    @get:StringRes
    abstract val title: Int

    @get:MenuRes
    abstract val menu: Int

    @get:StringRes
    abstract val drop: Int

    private val headerAdapter: HeaderAdapter = HeaderAdapter()

    private val contentAdapter: ContentAdapter = ContentAdapter()

    private lateinit var contract: ActivityResultLauncher<EditContract.Input>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { finish() }

        headerAdapter.isClickable = true
        headerAdapter.onClick = { header ->
            query(connection.schemaName!!, header.name, header.sort)
            headerAdapter.updateHeader(header)
        }

        contentAdapter.onCellClicked = { cell ->
            PreviewContentDialog
                .setCell(cell)
                .show(supportFragmentManager, null)
        }

        contract = registerForActivityResult(EditContract()) { shouldRefresh ->
            if (shouldRefresh) {
                contentAdapter.refresh()
            }
        }

        supportFragmentManager.setFragmentResultListener(DROP_CONTENT, this) { _, bundle ->
            bundle.getString(DROP_NAME)?.let { viewModel.drop(it) }
        }

        if (connection.hasSchemaData) {
            viewModel.databasePath = connection.databasePath!!
            viewModel.open()
            setupUi(
                connection.databasePath!!,
                connection.databaseName!!,
                connection.schemaName!!
            )

            viewModel.header(connection.schemaName!!)
        } else {
            showDatabaseParametersError()
        }
    }

    override fun onDestroy() {
        contract.unregister()
        super.onDestroy()
    }

    override fun onState(state: ContentState) {
        when (state) {
            is ContentState.Headers -> {
                headerAdapter.setItems(state.headers)

                contentAdapter.headersCount = state.headers.size

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
                        state.headers.size,
                        RecyclerView.VERTICAL,
                        false
                    )
                    recyclerView.adapter = ConcatAdapter(headerAdapter, contentAdapter)
                    recyclerView.edgeEffectFactory = BounceEdgeEffectFactory()
                }

                query(connection.schemaName!!)
            }
            is ContentState.Content -> {
//                if (this::contentAdapter.isInitialized.not()) {
//                    viewModel.header(connection.schemaName!!)
//                } else {
                lifecycleScope.launch {
                    contentAdapter.submitData(state.content)
                }
//                }
            }
        }
    }

    override fun onEvent(event: ContentEvent) {
        when (event) {
            is ContentEvent.Dropped -> {
                when (viewModel) {
                    is TableViewModel -> clearTable()
                    is TriggerViewModel -> dropTrigger()
                    is ViewViewModel -> dropView()
                }
            }
        }
    }

    private fun setupUi(databasePath: String, databaseName: String, schemaName: String) {
        with(binding.toolbar) {
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
                    R.id.edit -> {
                        showEdit(databasePath, databaseName)
                        true
                    }
                    else -> false
                }
            }
        }

        binding.recyclerView.setupAsTable()
    }

    private fun pragma(databaseName: String?, databasePath: String?, schemaName: String) {
        startActivity(
            Intent(this, PragmaActivity::class.java)
                .apply {
                    putExtra(Presentation.Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Presentation.Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Presentation.Constants.Keys.SCHEMA_NAME, schemaName)
                }
        )
    }

    private fun drop(name: String) =
        DropContentDialog
            .setMessage(drop, name)
            .show(supportFragmentManager, null)

    private fun query(
        name: String,
        orderBy: String? = null,
        sort: Sort = Sort.ASCENDING
    ) =
        viewModel.query(name, orderBy, sort)

    private fun clearTable() =
        contentAdapter.refresh()

    private fun dropTrigger() {
        setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(Presentation.Constants.Keys.SHOULD_REFRESH, true)
            }
        )
        finish()
    }

    private fun dropView() {
        setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(Presentation.Constants.Keys.SHOULD_REFRESH, true)
            }
        )
        finish()
    }

    private fun showEdit(databasePath: String, databaseName: String) =
        contract.launch(
            EditContract.Input(
                databasePath = databasePath,
                databaseName = databaseName
            )
        )
}
