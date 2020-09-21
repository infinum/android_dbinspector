package im.dino.dbinspector.ui.databases

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import im.dino.dbinspector.data.source.local.DatabaseHelper
import im.dino.dbinspector.databinding.DbinspectorActivityDatabasesBinding
import im.dino.dbinspector.domain.database.models.Database
import im.dino.dbinspector.domain.database.VersionOperation
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.tables.TablesActivity

class DatabasesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding = DbinspectorActivityDatabasesBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        DatabaseHelper.getDatabaseList(this)
            .map {
                Database(
                    absolutePath = it.absolutePath,
                    path = it.parentFile?.absolutePath.orEmpty(),
                    name = it.nameWithoutExtension,
                    version = VersionOperation()(it.absolutePath, null)
                )
            }
            .let {
                with(viewBinding.recyclerView) {
                    layoutManager = LinearLayoutManager(this@DatabasesActivity, LinearLayoutManager.VERTICAL, false)
                    adapter = DatabasesAdapter(it) { showTables(it) }
                }
            }
    }

    private fun showTables(database: Database) =
        startActivity(
            Intent(this, TablesActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE, database)
                }
        )
}