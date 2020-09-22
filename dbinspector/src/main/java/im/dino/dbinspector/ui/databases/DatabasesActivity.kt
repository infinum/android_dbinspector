package im.dino.dbinspector.ui.databases

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import im.dino.dbinspector.databinding.DbinspectorActivityDatabasesBinding
import im.dino.dbinspector.domain.database.models.Database
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.tables.TablesActivity

class DatabasesActivity : AppCompatActivity() {

    lateinit var viewBinding: DbinspectorActivityDatabasesBinding

    private val viewModel: DatabaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DbinspectorActivityDatabasesBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        viewModel.databases.observeForever {
            showDatabases(it)
        }

        viewModel.find()
    }

    private fun showDatabases(databases: List<Database>) {
        with(viewBinding.recyclerView) {
            layoutManager = LinearLayoutManager(this@DatabasesActivity, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
            adapter = DatabasesAdapter(databases) { showTables(it) }
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