package im.dino.dbinspector.ui.tables.content

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityContentBinding
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.tables.pragma.PragmaActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class ContentActivity : AppCompatActivity() {

    lateinit var viewBinding: DbinspectorActivityContentBinding

    private lateinit var viewModel: ContentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DbinspectorActivityContentBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        intent.extras?.let {
            val databaseName = it.getString(Constants.Keys.DATABASE_NAME)
            val databasePath = it.getString(Constants.Keys.DATABASE_PATH)
            val tableName = it.getString(Constants.Keys.TABLE_NAME)
            if (databaseName.isNullOrBlank().not() && databasePath.isNullOrBlank().not() && tableName.isNullOrBlank().not()) {
                viewModel = ViewModelProvider(
                    this,
                    ContentViewModelFactory(databasePath!!, tableName!!)
                ).get(ContentViewModel::class.java)

                setupUi(databasePath, databaseName!!, tableName)
            } else {
                showError()
            }
        } ?: showError()
    }

    private fun setupUi(databasePath: String, databaseName: String, tableName: String) {
        val tableHeaders = viewModel.header()
        val adapter = ContentAdapter(tableHeaders)

        with(viewBinding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = listOf(databaseName, tableName).joinToString(" / ")
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.clear -> {
                        clearTable(tableName)
                        true
                    }
                    R.id.pragma -> {
                        showTablePragma(databaseName, databasePath, tableName)
                        true
                    }
                    R.id.refresh -> {
                        adapter.submitData(lifecycle, PagingData.empty())
                        lifecycleScope.launch {
                            viewModel.query().collectLatest { data ->
                                adapter.submitData(data)
                            }
                        }
                        true
                    }
                    else -> false
                }
            }

            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                adapter.submitData(lifecycle, PagingData.empty())
                lifecycleScope.launch {
                    viewModel.query().collectLatest {
                        adapter.submitData(it)

                    }
                }
            }

            recyclerView.layoutManager = GridLayoutManager(recyclerView.context, tableHeaders.size)
            recyclerView.adapter = adapter

            lifecycleScope.launch {
                viewModel.query().collectLatest {
                    adapter.submitData(it)

                }
            }
        }
    }

    private fun showError() {
        with(viewBinding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = "Error"

            // TODO: push or show error views or Fragment
        }
    }

    private fun showTablePragma(databaseName: String?, databasePath: String?, tableName: String) {
        startActivity(
            Intent(this, PragmaActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Constants.Keys.TABLE_NAME, tableName)
                }
        )
    }

    private fun clearTable(name: String) =
        MaterialAlertDialogBuilder(this)
            .setMessage(String.format(getString(R.string.dbinspector_clear_table_confirm), name))
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                val ok = viewModel.clear()
                if (ok) {
                    (viewBinding.recyclerView.adapter as ContentAdapter).submitData(lifecycle, PagingData.empty())
                } else {
                    showError()
                }
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
            .show()
}