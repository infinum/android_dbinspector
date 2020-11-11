package com.infinum.dbinspector.ui.schema.shared

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorFragmentSchemaBinding
import com.infinum.dbinspector.ui.shared.Constants
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import com.infinum.dbinspector.ui.shared.searchable.BaseSearchableFragment

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

    private lateinit var schemaAdapter: SchemaAdapter

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
            schemaAdapter = SchemaAdapter(
                onClick = this@SchemaFragment::show
            )
            schemaAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
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

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            recyclerView.adapter = schemaAdapter

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

    override fun search(query: String?) {
        query(query)
//        schemaAdapter.refresh()
    }

    private fun query(query: String?) {
        viewModel.query(databasePath, query) {
            schemaAdapter.submitData(it)
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

    private fun showError() =
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setTitle(R.string.dbinspector_title_error)
            .setMessage(R.string.dbinspector_error_parameters)
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                requireActivity().finish()
            }
            .create()
            .show()
}
