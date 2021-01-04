package com.infinum.dbinspector.ui.edit

import android.content.DialogInterface
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivityEditBinding
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
        }

        with(binding) {
            editorInput.doOnTextChanged { text, _, _, _ ->
                executeButton.isEnabled = text.isNullOrBlank().not()
            }

            executeButton.setOnClickListener {
                query()
            }
            executeButton.setOnLongClickListener {
                showMockQueries()
                true
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

    private fun query() {
        val query = binding.editorInput.text?.toString().orEmpty().trim()

        viewModel.header(query) { tableHeaders ->
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
                onData = {
                    binding.errorView.isVisible = false
                    contentAdapter.submitData(it)
                },
                onError = {
                    binding.errorView.isVisible = true
                    binding.errorView.text = it.message
                }
            )
        }
    }

    private fun showMockQueries() {
        val mock = arrayOf(
            "SELECT * FROM users;",
            "SELECT * FROM artists;"
        )
        MaterialAlertDialogBuilder(this)
            .setItems(mock) { dialog: DialogInterface, selectedIndex: Int ->
                dialog.dismiss()
                binding.editorInput.setText(mock[selectedIndex])
            }
            .create()
            .show()
    }
}
