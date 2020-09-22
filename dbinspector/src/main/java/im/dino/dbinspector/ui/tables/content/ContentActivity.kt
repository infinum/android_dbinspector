package im.dino.dbinspector.ui.tables.content

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import im.dino.dbinspector.databinding.DbinspectorActivityContentBinding
import im.dino.dbinspector.domain.table.models.Table
import im.dino.dbinspector.ui.shared.Constants
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
            val table = it.getParcelable<Table>(Constants.Keys.TABLE)
            if (databasePath.isNullOrBlank().not() && table != null) {
                viewModel = ViewModelProvider(
                    this,
                    ContentViewModelFactory(databasePath.orEmpty(), table.name)
                ).get(ContentViewModel::class.java)
                setupUi(databaseName, table)
            } else {
                showError()
            }
        } ?: showError()
    }

    private fun setupUi(databaseName: String?, table: Table) {
        val tableHeaders = viewModel.header()

        with(viewBinding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = listOfNotNull(databaseName, table.name).joinToString(" / ")

            val adapter = ContentAdapter(tableHeaders)
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
}