package im.dino.dbinspector.ui.pragma.shared

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorFragmentPragmaBinding
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.base.BaseFragment
import im.dino.dbinspector.ui.shared.delegates.viewBinding
import im.dino.dbinspector.ui.shared.headers.HeaderAdapter

internal abstract class PragmaFragment :
    BaseFragment(R.layout.dbinspector_fragment_pragma) {

    companion object {

        fun bundle(databasePath: String, tableName: String): Bundle =
            Bundle().apply {
                putString(Constants.Keys.DATABASE_PATH, databasePath)
                putString(Constants.Keys.SCHEMA_NAME, tableName)
            }
    }

    private lateinit var databasePath: String

    private lateinit var tableName: String

    abstract val viewModel: PragmaSourceViewModel

    abstract fun headers(): List<String>

    private lateinit var pragmaAdapter: PragmaAdapter

    override val binding: DbinspectorFragmentPragmaBinding by viewBinding(
        DbinspectorFragmentPragmaBinding::bind
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            databasePath = it.getString(Constants.Keys.DATABASE_PATH, "")
            tableName = it.getString(Constants.Keys.SCHEMA_NAME, "")

            viewModel.databasePath = databasePath
        } ?: run {
            showError()
        }
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
            recyclerView.adapter = ConcatAdapter(HeaderAdapter(headers()), pragmaAdapter)

            query()
        }
    }

    private fun query() =
        viewModel.query(tableName) {
            pragmaAdapter.submitData(it)
        }

    private fun showError() {
        println("Some error")
    }
}