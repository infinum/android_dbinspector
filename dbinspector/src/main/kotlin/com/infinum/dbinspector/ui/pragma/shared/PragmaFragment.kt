package com.infinum.dbinspector.ui.pragma.shared

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorFragmentPragmaBinding
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.base.BaseFragment
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.headers.Header
import com.infinum.dbinspector.ui.shared.headers.HeaderAdapter

internal abstract class PragmaFragment :
    BaseFragment(R.layout.dbinspector_fragment_pragma) {

    companion object {

        fun bundle(databasePath: String, tableName: String): Bundle =
            Bundle().apply {
                putString(Presentation.Constants.Keys.DATABASE_PATH, databasePath)
                putString(Presentation.Constants.Keys.SCHEMA_NAME, tableName)
            }
    }

    private lateinit var databasePath: String

    private lateinit var tableName: String

    abstract override val viewModel: PragmaSourceViewModel

    abstract fun headers(): List<String>

    private lateinit var pragmaAdapter: PragmaAdapter

    override val binding: DbinspectorFragmentPragmaBinding by viewBinding(
        DbinspectorFragmentPragmaBinding::bind
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            databasePath = it.getString(Presentation.Constants.Keys.DATABASE_PATH, "")
            tableName = it.getString(Presentation.Constants.Keys.SCHEMA_NAME, "")

            viewModel.databasePath = databasePath
        } ?: (requireActivity() as? BaseActivity)?.showDatabaseParametersError()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pragmaAdapter = PragmaAdapter(headers().size)

        with(binding.recyclerView) {
            updateLayoutParams {
                minimumWidth = resources.displayMetrics.widthPixels
            }
        }

        with(binding) {
            pragmaAdapter.addLoadStateListener { loadState ->
                if (loadState.prepend.endOfPaginationReached) {
                    swipeRefresh.isRefreshing = loadState.refresh !is LoadState.NotLoading
                }
            }

            swipeRefresh.setOnRefreshListener {
                pragmaAdapter.refresh()
            }

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

            recyclerView.layoutManager = GridLayoutManager(recyclerView.context, headers().size)

            val headerAdapter = HeaderAdapter(headers().map { Header(name = it) }, false)

            recyclerView.adapter = ConcatAdapter(headerAdapter, pragmaAdapter)

            query()
        }
    }

    private fun query() =
        viewModel.query(tableName) {
            pragmaAdapter.submitData(it)
        }
}
