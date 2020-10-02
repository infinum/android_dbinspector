package im.dino.dbinspector.ui.schema.shared

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorFragmentSchemaBinding
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.base.searchable.BaseSearchableFragment
import im.dino.dbinspector.ui.shared.delegates.viewBinding

internal abstract class SchemaFragment :
    BaseSearchableFragment(R.layout.dbinspector_fragment_schema),
    SwipeRefreshLayout.OnRefreshListener {

    companion object {

        fun bundle(databasePath: String, databaseName: String): Bundle =
            Bundle().apply {
                putString(Constants.Keys.DATABASE_PATH, databasePath)
                putString(Constants.Keys.DATABASE_NAME, databaseName)
            }
    }

    private lateinit var databasePath: String

    private lateinit var databaseName: String

    abstract val viewModel: SchemaViewModel

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
            swipeRefresh.setOnRefreshListener(this@SchemaFragment)

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            recyclerView.adapter = SchemaAdapter(
                onClick = this@SchemaFragment::show
            )
            emptyLayout.retryButton.setOnClickListener {
                refresh()
            }

            query(searchQuery())
        }

        observe()
    }

    override fun onRefresh() {
        with(binding) {
            swipeRefresh.isRefreshing = false
            refresh()
        }
    }

    override fun search(query: String?) =
        query(query)

    private fun observe() {
        viewModel.observe {
            refresh()
        }
    }

    private fun refresh() {
        (binding.recyclerView.adapter as? SchemaAdapter)?.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
        query(searchQuery())
    }

    private fun query(query: String?) {
        with(binding) {
            viewModel.query(
                path = databasePath,
                argument = query,
                onData = {
                    (recyclerView.adapter as? SchemaAdapter)?.submitData(it)
                },
                onEmpty = {
                    emptyLayout.root.isVisible = it
                    swipeRefresh.isVisible = it.not()
                }
            )
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
