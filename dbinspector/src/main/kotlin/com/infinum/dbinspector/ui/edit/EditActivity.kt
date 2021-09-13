package com.infinum.dbinspector.ui.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivityEditBinding
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.extensions.setupAsTable
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.content.shared.ContentAdapter
import com.infinum.dbinspector.ui.content.shared.preview.PreviewContentDialog
import com.infinum.dbinspector.ui.edit.history.HistoryDialog
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.base.lifecycle.LifecycleConnection
import com.infinum.dbinspector.ui.shared.delegates.lifecycleConnection
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.edgefactories.bounce.BounceEdgeEffectFactory
import com.infinum.dbinspector.ui.shared.headers.HeaderAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
internal class EditActivity : BaseActivity<EditState, EditEvent>(), HistoryDialog.Listener {

    override val binding by viewBinding(DbinspectorActivityEditBinding::inflate)

    override val viewModel: EditViewModel by viewModel()

    private val connection: LifecycleConnection by lifecycleConnection()

    private val headerAdapter: HeaderAdapter = HeaderAdapter()

    private val contentAdapter: ContentAdapter = ContentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentAdapter.onCellClicked = { cell ->
            PreviewContentDialog
                .setCell(cell)
                .show(supportFragmentManager, null)
        }

        binding.toolbar.setNavigationOnClickListener { finish() }

        if (connection.hasDatabaseData) {
            viewModel.databasePath = connection.databasePath!!
            viewModel.open()
            viewModel.keywords()

            setupUi(connection.databaseName!!)
        } else {
            showDatabaseParametersError()
        }
    }

    override fun onState(state: EditState) {
        when (state) {
            is EditState.Headers -> {
                if (state.headers.isNotEmpty()) {
                    binding.errorView.isVisible = false
                    binding.recyclerView.layoutManager = GridLayoutManager(
                        this@EditActivity,
                        state.headers.size,
                        RecyclerView.VERTICAL,
                        false
                    )
                    binding.recyclerView.edgeEffectFactory = BounceEdgeEffectFactory()

                    headerAdapter.setItems(state.headers)

                    contentAdapter.headersCount = state.headers.size

                    with(binding) {
                        recyclerView.adapter = ConcatAdapter(
                            headerAdapter,
                            contentAdapter
                        )
                    }

                    val query = binding.editorInput.text?.toString().orEmpty().trim()
                    viewModel.query(query = query)
                } else {
                    viewModel.affectedRows()
                }
            }
            is EditState.Content -> showData(state.content)
            is EditState.AffectedRows -> showAffectedRows(state.affectedRows)
        }
    }

    override fun onEvent(event: EditEvent) {
        when (event) {
            is EditEvent.Keywords ->
                binding.editorInput.addKeywords(event.keywords)
            is EditEvent.History ->
                binding.toolbar.menu.findItem(R.id.history).isEnabled = event.history.executions.isNotEmpty()
            is EditEvent.SimilarExecution ->
                with(binding) {
                    suggestionButton.isVisible = event.history.executions.isNotEmpty() &&
                        editorInput.text?.toString()?.trim().orEmpty().isNotBlank() &&
                        (editorInput.text?.toString().orEmpty().trim() != suggestionButton.text)
                    suggestionButton.text = event.history.executions.firstOrNull()?.statement
                }
        }
    }

    override fun onError(error: Throwable) {
        showError(error.message)
    }

    override fun onHistorySelected(statement: String) {
        with(binding) {
            editorInput.setContent(statement)
            editorInput.setSelection(editorInput.text?.length ?: 0)
        }
    }

    private fun setupUi(databaseName: String) {
        with(binding.toolbar) {
            subtitle = databaseName
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.clear -> clearInput()
                    R.id.history -> showHistory()
                    R.id.execute -> query()
                }
                true
            }
        }

        with(binding) {
            suggestionButton.setOnClickListener {
                editorInput.setContent(suggestionButton.text.toString())
                editorInput.setSelection(editorInput.text?.length ?: 0)
                suggestionButton.isVisible = false
            }
            editorInput.doOnTextChanged { text, _, _, _ ->
                toolbar.menu.findItem(R.id.clear).isEnabled = text.isNullOrBlank().not()
                toolbar.menu.findItem(R.id.execute).isEnabled = text.isNullOrBlank().not()

                if (text?.toString()?.trim().orEmpty().isNotBlank()) {
                    viewModel.findSimilarExecution(lifecycleScope, text?.toString()?.trim().orEmpty())
                } else {
                    suggestionButton.isVisible = text?.toString()?.trim().orEmpty().isNotBlank()
                }
            }
        }

        binding.recyclerView.setupAsTable()
        viewModel.history()
    }

    private fun clearInput() =
        binding.editorInput.text.clear()

    private fun showHistory() =
        connection.databasePath?.let {
            HistoryDialog.show(
                connection.databaseName,
                it,
                supportFragmentManager
            )
        }

    private fun query() {
        val query = binding.editorInput.text?.toString().orEmpty().trim()
        viewModel.header(query = query)
    }

    private fun showData(cells: PagingData<Cell>) {
        viewModel.saveSuccessfulExecution(binding.editorInput.text?.toString().orEmpty().trim())
        with(binding) {
            recyclerView.isVisible = true
            affectedRowsView.isVisible = false
            errorView.isVisible = false
        }.also {
            lifecycleScope.launch {
                contentAdapter.submitData(cells)
            }
        }
    }

    private fun showAffectedRows(rowCount: String) {
        viewModel.saveSuccessfulExecution(binding.editorInput.text?.toString().orEmpty().trim())
        with(binding) {
            recyclerView.isVisible = false
            affectedRowsView.isVisible = true
            errorView.isVisible = false
            affectedRowsView.text =
                resources.getQuantityString(
                    R.plurals.dbinspector_affected_rows,
                    rowCount.toInt(),
                    rowCount
                )
        }
        setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(Presentation.Constants.Keys.SHOULD_REFRESH, true)
            }
        )
    }

    private fun showError(message: String?) {
        with(binding) {
            recyclerView.isVisible = false
            affectedRowsView.isVisible = false
            errorView.isVisible = true
            errorView.text = message
        }
        viewModel.saveFailedExecution(binding.editorInput.text?.toString().orEmpty().trim())
    }
}
