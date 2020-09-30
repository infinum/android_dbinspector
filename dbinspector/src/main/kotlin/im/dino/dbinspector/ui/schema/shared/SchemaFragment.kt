package im.dino.dbinspector.ui.schema.shared

import android.content.Context
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
import im.dino.dbinspector.ui.shared.Searchable
import im.dino.dbinspector.ui.shared.base.BaseFragment
import im.dino.dbinspector.ui.shared.delegates.viewBinding

internal abstract class SchemaFragment :
    BaseFragment(R.layout.dbinspector_fragment_schema),
    SwipeRefreshLayout.OnRefreshListener,
    Searchable {

    companion object {

        fun bundle(databasePath: String, databaseName: String): Bundle =
            Bundle().apply {
                putString(Constants.Keys.DATABASE_PATH, databasePath)
                putString(Constants.Keys.DATABASE_NAME, databaseName)
            }
    }

    internal lateinit var databasePath: String

    internal lateinit var databaseName: String

    private var parentSearchable: Searchable? = null

    abstract val viewModel: SchemaViewModel

    abstract fun itemClass(): Class<*>

    override val binding: DbinspectorFragmentSchemaBinding by viewBinding(
        DbinspectorFragmentSchemaBinding::bind
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)

        parentSearchable = (context as? Searchable)
    }

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
            swipeRefresh.setOnRefreshListener(this@SchemaFragment)

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            recyclerView.adapter = SchemaAdapter(
                onClick = this@SchemaFragment::show
            )
            emptyLayout.retryButton.setOnClickListener {
                refresh()
            }

            query(parentSearchable?.searchQuery())
        }

        observe()
    }

    override fun onDetach() {
        parentSearchable = null
        super.onDetach()
    }

    override fun onRefresh() {
        with(binding) {
            swipeRefresh.isRefreshing = false
            refresh()
        }
    }

    override fun onSearchOpened() = Unit

    override fun search(query: String?) =
        query(query)

    override fun searchQuery(): String? = null

    override fun onSearchClosed() = Unit

    private fun observe() {
        viewModel.observe {
            refresh()
        }
    }

    private fun refresh() {
        (binding.recyclerView.adapter as? SchemaAdapter)?.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
        query(parentSearchable?.searchQuery())
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
            Intent(requireContext(), itemClass())
                .apply {
                    putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Constants.Keys.SCHEMA_NAME, name)
                }
        )
}