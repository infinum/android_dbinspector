package im.dino.dbinspector.ui.tables.pragma

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import im.dino.dbinspector.databinding.DbinspectorActivityPragmaBinding
import im.dino.dbinspector.domain.table.models.Table
import im.dino.dbinspector.ui.shared.Constants

class PragmaActivity : AppCompatActivity() {

    lateinit var viewBinding: DbinspectorActivityPragmaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DbinspectorActivityPragmaBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        intent.extras?.let {
            val databasePath = it.getString(Constants.Keys.DATABASE_PATH)
            val table = it.getParcelable<Table>(Constants.Keys.TABLE)
            if (databasePath.isNullOrBlank().not() && table != null) {
                setupUi(databasePath.orEmpty(), table)
            } else {
                showError()
            }
        } ?: showError()
    }

    private fun setupUi(databasePath: String, table: Table) {
        with(viewBinding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = table.name

            tabLayout.setupWithViewPager(viewPager)
            viewPager.adapter = PragmaTypeAdapter(
                context = this@PragmaActivity,
                fragmentManager = supportFragmentManager,
                databasePath = databasePath,
                tableName = table.name
            )
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