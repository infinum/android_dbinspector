package im.dino.dbinspector.ui.pragma

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityPragmaBinding
import im.dino.dbinspector.ui.pragma.shared.PragmaTypeAdapter
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.base.BaseActivity
import im.dino.dbinspector.ui.shared.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class PragmaActivity : BaseActivity() {

    override val binding by viewBinding(DbinspectorActivityPragmaBinding::inflate)

    private val viewModel: PragmaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            val databaseName = it.getString(Constants.Keys.DATABASE_NAME)
            val databasePath = it.getString(Constants.Keys.DATABASE_PATH)
            val tableName = it.getString(Constants.Keys.SCHEMA_NAME)
            if (
                databaseName.isNullOrBlank().not() &&
                databasePath.isNullOrBlank().not() &&
                tableName.isNullOrBlank().not()
            ) {
                setupUi(databaseName!!, databasePath!!, tableName!!)
            } else {
                showError()
            }
        } ?: showError()
    }

    override fun onStop() {
        super.onStop()
        viewModel.close(lifecycleScope)
    }

    private fun setupUi(databaseName: String, databasePath: String, tableName: String) {
        viewModel.databasePath = databasePath
        viewModel.open(lifecycleScope)

        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = listOf(databaseName, tableName).joinToString(" / ")
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.refresh -> {
                        refreshChildren()
                        true
                    }
                    else -> false
                }
            }

            tabLayout.setupWithViewPager(viewPager)
            viewPager.adapter = PragmaTypeAdapter(
                context = this@PragmaActivity,
                fragmentManager = supportFragmentManager,
                databasePath = databasePath,
                tableName = tableName
            )
        }
    }

    private fun showError() {
        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
        }
    }

    private fun refreshChildren() =
        supportFragmentManager
            .fragments
            .filterIsInstance<SwipeRefreshLayout.OnRefreshListener>()
            .forEach(SwipeRefreshLayout.OnRefreshListener::onRefresh)
}
