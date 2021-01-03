package com.infinum.dbinspector.ui.edit

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
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivityEditBinding
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.content.shared.ContentAdapter
import com.infinum.dbinspector.ui.content.shared.ContentPreviewFactory
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.headers.Header
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

                // TODO: get size and column names dynamically
                val tableHeaders = (0..12).map { name ->
                    Header(
                        name = name.toString()
                    )
                }

                contentAdapter = ContentAdapter(
                    headersCount = tableHeaders.size,
                    onCellClicked = { cell -> contentPreviewFactory.showCell(cell) }
                )

                with(binding) {
                    recyclerView.layoutManager = GridLayoutManager(
                        this@EditActivity,
                        tableHeaders.size,
                        RecyclerView.VERTICAL,
                        false
                    )

                    recyclerView.adapter = ConcatAdapter(
                        HeaderAdapter(tableHeaders, false) {},
                        contentAdapter
                    )
                }
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
                editorInput.setText("SELECT * FROM users;")
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

    private fun query() =
        viewModel.query(
            query = binding.editorInput.text?.toString().orEmpty().trim(),
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
