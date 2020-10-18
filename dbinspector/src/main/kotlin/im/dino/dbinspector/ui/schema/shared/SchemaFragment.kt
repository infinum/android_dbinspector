package im.dino.dbinspector.ui.schema.shared

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorFragmentSchemaBinding
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.delegates.viewBinding
import im.dino.dbinspector.ui.shared.searchable.BaseSearchableFragment

internal abstract class SchemaFragment :
    BaseSearchableFragment(R.layout.dbinspector_fragment_schema) {

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

    abstract val viewModel: SchemaSourceViewModel

    abstract fun childView(): Class<*>

    override val binding: DbinspectorFragmentSchemaBinding by viewBinding(
        DbinspectorFragmentSchemaBinding::bind
    )

    private lateinit var contract: ActivityResultLauncher<SchemaContract.Input>

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

            contract = registerForActivityResult(SchemaContract()) { shouldRefresh ->
                if (shouldRefresh) {
                    adapter.refresh()
                }
            }
        }

        query(searchQuery())
    }

    override fun onDestroyView() {
        contract.unregister()
        super.onDestroyView()
    }

    override fun search(query: String?) {
        query(query)
        (binding.recyclerView.adapter as? SchemaAdapter)?.refresh()
    }

    private fun query(query: String?) {
        viewModel.query(databasePath) {
            with(binding) {
                (recyclerView.adapter as? SchemaAdapter)?.submitData(it)
            }
        }
    }

    private fun show(name: String) =
        contract.launch(
            SchemaContract.Input(
                childView = childView(),
                databasePath = databasePath,
                databaseName = databaseName,
                schemaName = name
            )
        )

    private fun showError() {
        println("Some error")
    }
}
