package im.dino.dbinspector.ui.tables.pragma.foreignkeys

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorFragmentPragmaBinding
import im.dino.dbinspector.domain.pragma.models.ForeignKeyListColumns
import im.dino.dbinspector.ui.shared.BaseFragment
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.viewBinding
import im.dino.dbinspector.ui.tables.pragma.PragmaAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class ForeignKeysFragment : BaseFragment(R.layout.dbinspector_fragment_pragma), SwipeRefreshLayout.OnRefreshListener {

    companion object {

        fun newInstance(databasePath: String, tableName: String): ForeignKeysFragment =
            ForeignKeysFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.Keys.DATABASE_PATH, databasePath)
                    putString(Constants.Keys.TABLE_NAME, tableName)
                }
            }
    }

    private lateinit var databasePath: String
    private lateinit var tableName: String

    override val binding: DbinspectorFragmentPragmaBinding by viewBinding(
        DbinspectorFragmentPragmaBinding::bind
    )

    private val viewModel by viewModels<ForeignKeyViewModel>()

    private val adapter = PragmaAdapter(ForeignKeyListColumns.values().map { it.name.toLowerCase() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            databasePath = it.getString(Constants.Keys.DATABASE_PATH, "")
            tableName = it.getString(Constants.Keys.TABLE_NAME, "")
        } ?: run {
            // TODO: Show error state
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            swipeRefresh.setOnRefreshListener(this@ForeignKeysFragment)

            recyclerView.layoutManager = GridLayoutManager(recyclerView.context, ForeignKeyListColumns.values().size)
            recyclerView.adapter = adapter

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.query(databasePath, tableName).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onRefresh() {
        with(binding) {
            swipeRefresh.isRefreshing = false
            adapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.query(databasePath, tableName).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }
}