package com.infinum.dbinspector.ui.schema.shared

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorFragmentSchemaBinding
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.edgefactories.bounce.BounceEdgeEffectFactory
import com.infinum.dbinspector.ui.shared.searchable.BaseSearchableFragment
import kotlinx.coroutines.launch

internal abstract class SchemaFragment :
    BaseSearchableFragment<SchemaState, Any>(R.layout.dbinspector_fragment_schema) {

    companion object {

        fun bundle(databasePath: String, databaseName: String): Bundle =
            Bundle().apply {
                putString(Presentation.Constants.Keys.DATABASE_PATH, databasePath)
                putString(Presentation.Constants.Keys.DATABASE_NAME, databaseName)
            }
    }

    private lateinit var databasePath: String

    private lateinit var databaseName: String

    abstract var statement: String

    abstract override val viewModel: SchemaSourceViewModel

    abstract fun childView(): Class<*>

    override val binding: DbinspectorFragmentSchemaBinding by viewBinding(
        DbinspectorFragmentSchemaBinding::bind
    )

    private lateinit var contract: ActivityResultLauncher<SchemaContract.Input>

    private lateinit var schemaAdapter: SchemaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            databasePath = it.getString(Presentation.Constants.Keys.DATABASE_PATH, "")
            databaseName = it.getString(Presentation.Constants.Keys.DATABASE_NAME, "")
        } ?: (requireActivity() as? BaseActivity<*, *>)?.showDatabaseParametersError()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            schemaAdapter = SchemaAdapter(
                onClick = this@SchemaFragment::show
            )
            schemaAdapter.addLoadStateListener { loadState ->
                if (loadState.append.endOfPaginationReached) {
                    val isEmpty = schemaAdapter.itemCount < 1
                    emptyLayout.root.isVisible = isEmpty
                    swipeRefresh.isVisible = isEmpty.not()
                }
                if (loadState.prepend.endOfPaginationReached) {
                    swipeRefresh.isRefreshing = loadState.refresh !is LoadState.NotLoading
                }
            }

            swipeRefresh.setOnRefreshListener {
                schemaAdapter.refresh()
            }

            emptyLayout.retryButton.setOnClickListener {
                schemaAdapter.refresh()
            }

            with(recyclerView) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
                adapter = schemaAdapter
                edgeEffectFactory = BounceEdgeEffectFactory()
            }

            contract = registerForActivityResult(SchemaContract()) { shouldRefresh ->
                if (shouldRefresh) {
                    schemaAdapter.refresh()
                }
            }
        }

        query(searchQuery())
    }

    override fun onDestroyView() {
        contract.unregister()
        super.onDestroyView()
    }

    override fun onState(state: SchemaState) {
        when (state) {
            is SchemaState.Schema ->
                viewLifecycleOwner.lifecycleScope.launch {
                    schemaAdapter.submitData(state.schema)
                }
        }
    }

    override fun onEvent(event: Any) = Unit

    override fun search(query: String?) {
        query(query)
    }

    fun refresh() = schemaAdapter.refresh()

    private fun query(query: String?) {
        viewModel.query(databasePath, query)
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
}
