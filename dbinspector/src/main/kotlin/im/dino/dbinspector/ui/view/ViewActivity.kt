package im.dino.dbinspector.ui.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityViewBinding
import im.dino.dbinspector.ui.pragma.PragmaActivity
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.content.ContentAdapter

internal class ViewActivity : AppCompatActivity() {

    lateinit var binding: DbinspectorActivityViewBinding

    private lateinit var viewModel: ViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DbinspectorActivityViewBinding.inflate(layoutInflater)

        setContentView(binding.root)

        intent.extras?.let {
            val databaseName = it.getString(Constants.Keys.DATABASE_NAME)
            val databasePath = it.getString(Constants.Keys.DATABASE_PATH)
            val tableName = it.getString(Constants.Keys.VIEW_NAME)
            if (databaseName.isNullOrBlank().not() && databasePath.isNullOrBlank().not() && tableName.isNullOrBlank().not()) {
                viewModel = ViewModelProvider(
                    this,
                    ViewViewModelFactory(databasePath!!, tableName!!)
                ).get(ViewViewModel::class.java)

                setupToolbar(databasePath, databaseName!!, tableName)
                setupSwipeRefresh()
                setupRecyclerView()
            } else {
                showError()
            }
        } ?: showError()
    }

    private fun setupToolbar(databasePath: String, databaseName: String, tableName: String) =
        with(binding.toolbar) {
            setNavigationOnClickListener { finish() }
            subtitle = listOf(databaseName, tableName).joinToString(" / ")
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.drop -> {
                        dropView(tableName)
                        true
                    }
                    R.id.pragma -> {
                        showPragma(databaseName, databasePath, tableName)
                        true
                    }
                    R.id.refresh -> {
                        (binding.recyclerView.adapter as? ContentAdapter)?.submitData(lifecycle, PagingData.empty())
                        query()
                        true
                    }
                    else -> false
                }
            }
        }

    private fun setupSwipeRefresh() =
        with(binding.swipeRefresh) {
            setOnRefreshListener {
                isRefreshing = false

                (binding.recyclerView.adapter as? ContentAdapter)?.let { adapter ->
                    adapter.submitData(lifecycle, PagingData.empty())
                    query()
                }
            }
        }

    private fun setupRecyclerView() =
        with(binding.recyclerView) {
            viewModel.header { tableHeaders ->
                layoutManager = GridLayoutManager(context, tableHeaders.size)
                adapter = ContentAdapter(tableHeaders)

                query()
            }
        }

    private fun showError() {

    }

    private fun showPragma(databaseName: String?, databasePath: String?, schemaName: String) {
        startActivity(
            Intent(this, PragmaActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_NAME, databaseName)
                    putExtra(Constants.Keys.DATABASE_PATH, databasePath)
                    putExtra(Constants.Keys.SCHEMA_NAME, schemaName)
                }
        )
    }

    private fun dropView(name: String) =
        MaterialAlertDialogBuilder(this)
            .setMessage(String.format(getString(R.string.dbinspector_drop_view_confirm), name))
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                drop()
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
            .show()

    private fun query() =
        viewModel.query() {
            (binding.recyclerView.adapter as? ContentAdapter)?.submitData(it)
        }

    private fun drop() =
        viewModel.drop() {
            finish()
        }
}