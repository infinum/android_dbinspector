package im.dino.dbinspector.ui.schema.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorFragmentSchemaBinding
import im.dino.dbinspector.ui.schema.SchemaAdapter
import im.dino.dbinspector.ui.schema.shared.SchemaFragment
import im.dino.dbinspector.ui.view.ViewActivity
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.delegates.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

internal class ViewsFragment :
    SchemaFragment(R.layout.dbinspector_fragment_schema),
    SwipeRefreshLayout.OnRefreshListener {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): ViewsFragment =
            ViewsFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.Keys.DATABASE_PATH, databasePath)
                    putString(Constants.Keys.DATABASE_NAME, databaseName)
                }
            }
    }

    private lateinit var databasePath: String

    private lateinit var databaseName: String

    private val viewModel by viewModels<ViewsViewModel>()

    override val binding: DbinspectorFragmentSchemaBinding by viewBinding(
        DbinspectorFragmentSchemaBinding::bind
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            databasePath = it.getString(Constants.Keys.DATABASE_PATH, "")
            databaseName = it.getString(Constants.Keys.DATABASE_NAME, "")
        } ?: run {
            // TODO: Show error state
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            swipeRefresh.setOnRefreshListener(this@ViewsFragment)

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            recyclerView.adapter = SchemaAdapter(
                onClick = {
                    showViewContent(databaseName, databasePath, it)
                }
            )

            query()
        }

        observe()
    }

    override fun onRefresh() {
        with(binding) {
            swipeRefresh.isRefreshing = false

            (recyclerView.adapter as? SchemaAdapter)?.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())

            query()
        }
    }

    override fun search(query: String?) =
        viewModel.query(databasePath, query) {
            (binding.recyclerView.adapter as? SchemaAdapter)?.submitData(it)
        }

    private fun showViewContent(databaseName: String, databasePath: String, viewName: String) =
        startActivity(
            Intent(requireContext(), ViewActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Constants.Keys.VIEW_NAME, viewName)
                }
        )

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun observe() =
        viewModel.observe {
            (binding.recyclerView.adapter as? SchemaAdapter)?.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())

            query()
        }

    private fun query() {
        with(binding) {
            viewModel.query(
                databasePath,
                parentSearchable?.searchQuery()
            ) {
                (recyclerView.adapter as? SchemaAdapter)?.submitData(it)
            }
        }
    }
}