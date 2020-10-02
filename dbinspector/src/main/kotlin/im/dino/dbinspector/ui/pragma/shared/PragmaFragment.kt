package im.dino.dbinspector.ui.pragma.shared

import android.os.Bundle
import android.view.View
import androidx.core.view.updateLayoutParams
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorFragmentPragmaBinding
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.base.BaseFragment
import im.dino.dbinspector.ui.shared.delegates.viewBinding

internal abstract class PragmaFragment :
    BaseFragment(R.layout.dbinspector_fragment_pragma),
    SwipeRefreshLayout.OnRefreshListener {

    companion object {

        fun bundle(databasePath: String, tableName: String): Bundle =
            Bundle().apply {
                putString(Constants.Keys.DATABASE_PATH, databasePath)
                putString(Constants.Keys.SCHEMA_NAME, tableName)
            }
    }

    private lateinit var databasePath: String

    private lateinit var tableName: String

    abstract val viewModel: PragmaViewModel

    abstract fun headers(): List<String>

    override val binding: DbinspectorFragmentPragmaBinding by viewBinding(
        DbinspectorFragmentPragmaBinding::bind
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            databasePath = it.getString(Constants.Keys.DATABASE_PATH, "")
            tableName = it.getString(Constants.Keys.SCHEMA_NAME, "")
        } ?: run {
            showError()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.recyclerView) {
            updateLayoutParams {
                minimumWidth = resources.displayMetrics.widthPixels
            }
        }

        with(binding) {
            swipeRefresh.setOnRefreshListener(this@PragmaFragment)

            recyclerView.layoutManager = GridLayoutManager(recyclerView.context, headers().size)
            recyclerView.adapter = PragmaAdapter(headers())

            query()
        }
    }

    override fun onRefresh() {
        with(binding) {
            swipeRefresh.isRefreshing = false

            (recyclerView.adapter as? PragmaAdapter)?.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())

            query()
        }
    }

    private fun query() =
        viewModel.query(databasePath, tableName) {
            (binding.recyclerView.adapter as? PragmaAdapter)?.submitData(it)
        }

    private fun showError() {
        println("Some error")
    }
}
