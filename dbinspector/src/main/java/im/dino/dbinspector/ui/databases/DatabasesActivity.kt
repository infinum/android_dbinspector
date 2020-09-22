package im.dino.dbinspector.ui.databases

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityDatabasesBinding
import im.dino.dbinspector.domain.database.models.Database
import im.dino.dbinspector.extensions.databaseDir
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.tables.TablesActivity
import java.io.File
import java.io.FileOutputStream


class DatabasesActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_IMPORT = 666
    }

    lateinit var viewBinding: DbinspectorActivityDatabasesBinding

    private val viewModel: DatabaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DbinspectorActivityDatabasesBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        setupUi()

        viewModel.databases.observeForever {
            showDatabases(it)
        }

        viewModel.find()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMPORT) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.clipData?.let {
                        val uris = mutableListOf<Uri>()
                        for (i in 0 until it.itemCount) {
                            uris.add(it.getItemAt(i).uri)
                        }
                        importFiles(uris.toList())
                    } ?: data?.data?.let {
                        importFiles(listOf(it))
                    } ?: showError()
                }
                Activity.RESULT_CANCELED -> Unit
                else -> Unit
            }
        }
    }

    private fun setupUi() {
        with(viewBinding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.dbImport -> {
                        importDatabase()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showDatabases(databases: List<Database>) {
        with(viewBinding.recyclerView) {
            layoutManager = LinearLayoutManager(this@DatabasesActivity, LinearLayoutManager.VERTICAL, false)
            adapter = DatabasesAdapter(
                items = databases,
                onClick = { showTables(it) },
                onDelete = { removeDatabase(it) },
                onRename = { renameDatabase(it) },
                onCopy = { copyDatabase(it) },
                onShare = { shareDatabase(it) }
            )
        }
    }

    private fun showTables(database: Database) =
        startActivity(
            Intent(this, TablesActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE, database)
                }
        )

    private fun importDatabase() =
        startActivityForResult(
            Intent.createChooser(
                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                    type = "application/vnd.sqlite3"
//                    type = "application/x-sqlite3"
                    type = "application/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                },
                getString(R.string.dbinspector_action_import)
            ),
            REQUEST_CODE_IMPORT
        )

    private fun showError() {
        with(viewBinding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.subtitle = "Error"

            // TODO: push or show error views or Fragment
        }
    }

    private fun importFiles(uris: List<Uri>) {
        uris.forEach {
            it.lastPathSegment?.split("/")?.last()?.let { filename ->
                contentResolver.openInputStream(it)?.use { inputStream ->
                    FileOutputStream(File(databaseDir(), filename))
                        .use { outputStream ->
                            outputStream.write(inputStream.readBytes())
                        }
                }
            }
        }.also {
            viewModel.find()
        }
    }

    private fun removeDatabase(database: Database) =
        MaterialAlertDialogBuilder(this)
            .setMessage(String.format(getString(R.string.dbinspector_delete_database_confirm), database.name))
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                val ok = this.deleteDatabase("${database.name}.${database.extension}")
                if (ok) {
                    viewModel.find()
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

    private fun renameDatabase(database: Database) {

    }

    private fun copyDatabase(database: Database) {

    }

    private fun shareDatabase(database: Database) =
        startActivity(
            ShareCompat.IntentBuilder.from(this)
                .setType("application/octet-stream")
                .setStream(FileProvider.getUriForFile(this, "${this.packageName}.provider.database", File(database.absolutePath)))
                .intent.apply {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
        )
}