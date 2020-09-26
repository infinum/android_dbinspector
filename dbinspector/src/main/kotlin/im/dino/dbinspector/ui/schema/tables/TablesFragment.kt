package im.dino.dbinspector.ui.schema.tables

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
import im.dino.dbinspector.ui.table.TableActivity
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.delegates.viewBinding

internal class TablesFragment :
    SchemaFragment(R.layout.dbinspector_fragment_schema),
    SwipeRefreshLayout.OnRefreshListener {

    companion object {

        fun newInstance(databasePath: String, databaseName: String): TablesFragment =
            TablesFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.Keys.DATABASE_PATH, databasePath)
                    putString(Constants.Keys.DATABASE_NAME, databaseName)
                }
            }
    }

    private lateinit var databasePath: String

    private lateinit var databaseName: String

    private val viewModel by viewModels<TablesViewModel>()

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
            swipeRefresh.setOnRefreshListener(this@TablesFragment)

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            recyclerView.adapter = SchemaAdapter(
                onClick = {
                    showTableContent(databaseName, databasePath, it)
                }
            )

            query()
        }
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

    private fun showTableContent(databaseName: String, databasePath: String, tableName: String) =
        startActivity(
            Intent(requireContext(), TableActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Constants.Keys.TABLE_NAME, tableName)
                }
        )

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