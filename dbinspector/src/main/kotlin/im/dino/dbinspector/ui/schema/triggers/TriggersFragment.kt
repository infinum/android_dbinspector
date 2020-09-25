package im.dino.dbinspector.ui.schema.triggers

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
import im.dino.dbinspector.ui.schema.tables.content.ContentActivity
import im.dino.dbinspector.ui.shared.BaseFragment
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.viewBinding

internal class TriggersFragment : BaseFragment(R.layout.dbinspector_fragment_schema), SwipeRefreshLayout.OnRefreshListener {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): TriggersFragment =
            TriggersFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.Keys.DATABASE_PATH, databasePath)
                    putString(Constants.Keys.DATABASE_NAME, databaseName)
                }
            }
    }

    private lateinit var databasePath: String
    private lateinit var databaseName: String

    private val viewModel by viewModels<TriggersViewModel>()

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
            swipeRefresh.setOnRefreshListener(this@TriggersFragment)

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            recyclerView.adapter = SchemaAdapter(
                onClick = {
                    showTableContent(databaseName, databasePath, it)
                }
            )

            query(databasePath)
        }
    }

    override fun onRefresh() {
        with(binding) {
            swipeRefresh.isRefreshing = false

            (recyclerView.adapter as? SchemaAdapter)?.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())

            query(databasePath)
        }
    }

    private fun showTableContent(databaseName: String, databasePath: String, tableName: String) {
        startActivity(
            Intent(requireContext(), ContentActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Constants.Keys.TABLE_NAME, tableName)
                }
        )
    }

//    private fun search(database: Database, query: String?) =
//        viewModel.query(database.absolutePath, query) {
//            (binding.recyclerView.adapter as? TablesAdapter)?.submitData(it)
//        }

    private fun query(databasePath: String) {
        with(binding) {
            viewModel.query(
                databasePath,
                null
//                toolbar.menu.searchView?.query?.toString()
            ) {
                (recyclerView.adapter as? SchemaAdapter)?.submitData(it)
            }
        }
    }
}