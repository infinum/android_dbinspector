package com.infinum.dbinspector.ui.edit

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivityEditBinding
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.content.shared.ContentAdapter
import com.infinum.dbinspector.ui.content.shared.ContentPreviewFactory
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.headers.HeaderAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class EditActivity : BaseActivity() {

    override val binding by viewBinding(DbinspectorActivityEditBinding::inflate)

    private val viewModel: EditViewModel by viewModel()

    private lateinit var contentPreviewFactory: ContentPreviewFactory

    private lateinit var headerAdapter: HeaderAdapter

    private lateinit var contentAdapter: ContentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentPreviewFactory = ContentPreviewFactory(this)

        intent.extras?.let {
            val databaseName = it.getString(Presentation.Constants.Keys.DATABASE_NAME)
            val databasePath = it.getString(Presentation.Constants.Keys.DATABASE_PATH)
            if (
                databaseName.isNullOrBlank().not() &&
                databasePath.isNullOrBlank().not()
            ) {
                viewModel.databasePath = databasePath!!

                viewModel.open(lifecycleScope)

                viewModel.keywords { keywords ->
                    binding.editorInput.addKeywords(keywords)
                }

                setupUi(databaseName!!)
            }
        }
    }

    override fun onDestroy() {
        viewModel.close(lifecycleScope)
        super.onDestroy()
    }

    private fun setupUi(databaseName: String) {
        with(binding.toolbar) {
            setNavigationOnClickListener { finish() }
            subtitle = databaseName
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.clear -> clearInput()
                    R.id.execute -> query()
                }
                true
            }
        }

        with(binding) {
            editorInput.doOnTextChanged { text, _, _, _ ->
                toolbar.menu.findItem(R.id.clear).isEnabled = text.isNullOrBlank().not()
                toolbar.menu.findItem(R.id.execute).isEnabled = text.isNullOrBlank().not()
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

    private fun clearInput() =
        binding.editorInput.text.clear()

    private fun query() {
        val query = binding.editorInput.text?.toString().orEmpty().trim()

        viewModel.header(
            query = query,
            onData = { tableHeaders ->
                if (tableHeaders.isNotEmpty()) {
                    binding.errorView.isVisible = false
                    binding.recyclerView.layoutManager = GridLayoutManager(
                        this@EditActivity,
                        tableHeaders.size,
                        RecyclerView.VERTICAL,
                        false
                    )

                    headerAdapter = HeaderAdapter(tableHeaders, false) {}

                    contentAdapter = ContentAdapter(
                        headersCount = tableHeaders.size,
                        onCellClicked = { cell -> contentPreviewFactory.showCell(cell) }
                    )

                    with(binding) {
                        recyclerView.adapter = ConcatAdapter(
                            headerAdapter,
                            contentAdapter
                        )
                    }

                    viewModel.query(
                        query = query,
                        onData = { showData(it) },
                        onError = { showError(it.message) }
                    )
                } else {
                    viewModel.affectedRows(
                        onData = { showAffectedRows(it) },
                        onError = { showError(it.message) }
                    )
                }
            },
            onError = { showError(it.message) }
        )
    }

    private suspend fun showData(cells: PagingData<Cell>) =
        with(binding) {
            recyclerView.isVisible = true
            affectedRowsView.isVisible = false
            errorView.isVisible = false
        }.also {
            contentAdapter.submitData(cells)
        }

    private fun showAffectedRows(rowCount: String) =
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

    private fun showError(message: String?) =
        with(binding) {
            recyclerView.isVisible = false
            affectedRowsView.isVisible = false
            errorView.isVisible = true
            errorView.text = message
        }
}
