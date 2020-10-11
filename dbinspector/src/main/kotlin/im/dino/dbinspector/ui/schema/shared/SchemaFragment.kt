package im.dino.dbinspector.ui.schema.shared

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorFragmentSchemaBinding
import im.dino.dbinspector.ui.shared.base.DataSourceViewModel
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.base.Refreshable
import im.dino.dbinspector.ui.shared.base.searchable.BaseSearchableFragment
import im.dino.dbinspector.ui.shared.delegates.viewBinding

internal abstract class SchemaFragment :
    BaseSearchableFragment(R.layout.dbinspector_fragment_schema),
    Refreshable {

    companion object {

        fun bundle(databasePath: String, databaseName: String): Bundle =
            Bundle().apply {
                putString(Constants.Keys.DATABASE_PATH, databasePath)
                putString(Constants.Keys.DATABASE_NAME, databaseName)
            }
    }

    private lateinit var databasePath: String

    private lateinit var databaseName: String

    abstract var statement: String

    abstract val viewModel: DataSourceViewModel

    abstract fun childView(): Class<*>

    override val binding: DbinspectorFragmentSchemaBinding by viewBinding(
        DbinspectorFragmentSchemaBinding::bind
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            databasePath = it.getString(Constants.Keys.DATABASE_PATH, "")
            databaseName = it.getString(Constants.Keys.DATABASE_NAME, "")
        } ?: run {
            showError()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val adapter = SchemaAdapter(
                onClick = this@SchemaFragment::show
            )
            adapter.addLoadStateListener { loadState ->
                if (loadState.append.endOfPaginationReached) {
                    val isEmpty = adapter.itemCount < 1
                    emptyLayout.root.isVisible = isEmpty
                    swipeRefresh.isVisible = isEmpty.not()
                }
                if (loadState.prepend.endOfPaginationReached) {
                    swipeRefresh.isRefreshing = loadState.refresh !is LoadState.NotLoading
                }
            }

            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
            }

            emptyLayout.retryButton.setOnClickListener {
                adapter.refresh()
            }

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            recyclerView.adapter = adapter
        }

        query(searchQuery())

        observe()
    }

    override fun search(query: String?) {
        query(query)
        (binding.recyclerView.adapter as? SchemaAdapter)?.refresh()
    }

    override fun doRefresh() {
        with(binding) {
            swipeRefresh.isRefreshing = true
            (recyclerView.adapter as? SchemaAdapter)?.refresh()
        }
    }

    private fun observe() {
        viewModel.observe {
            (binding.recyclerView.adapter as? SchemaAdapter)?.refresh()
        }
    }

    private fun query(query: String?) {
        viewModel.query(databasePath) {
            with(binding) {
                (recyclerView.adapter as? SchemaAdapter)?.submitData(it)
            }
        }
    }

    private fun show(name: String) =
        startActivity(
            Intent(requireContext(), childView())
                .apply {
                    putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Constants.Keys.SCHEMA_NAME, name)
                }
        )

    private fun showError() {
        println("Some error")
    }
}
