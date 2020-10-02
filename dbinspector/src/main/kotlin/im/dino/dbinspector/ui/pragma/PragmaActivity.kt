package im.dino.dbinspector.ui.pragma

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityPragmaBinding
import im.dino.dbinspector.ui.pragma.shared.PragmaTypeAdapter
import im.dino.dbinspector.ui.shared.Constants

internal class PragmaActivity : AppCompatActivity() {

    lateinit var viewBinding: DbinspectorActivityPragmaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DbinspectorActivityPragmaBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

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

    private fun setupUi(databaseName: String, databasePath: String, tableName: String) {
        with(viewBinding) {
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
        with(viewBinding) {
            toolbar.setNavigationOnClickListener { finish() }
        }
    }

    private fun refreshChildren() =
        supportFragmentManager
            .fragments
            .filterIsInstance<SwipeRefreshLayout.OnRefreshListener>()
            .forEach(SwipeRefreshLayout.OnRefreshListener::onRefresh)
}
