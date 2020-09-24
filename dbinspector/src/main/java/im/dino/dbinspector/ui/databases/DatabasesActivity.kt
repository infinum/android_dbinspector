package im.dino.dbinspector.ui.databases

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityDatabasesBinding
import im.dino.dbinspector.databinding.DbinspectorDialogRenameBinding
import im.dino.dbinspector.domain.database.models.Database
import im.dino.dbinspector.extensions.scale
import im.dino.dbinspector.extensions.searchView
import im.dino.dbinspector.extensions.setup
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.Searchable
import im.dino.dbinspector.ui.tables.TablesActivity
import java.io.File

internal class DatabasesActivity : AppCompatActivity(), Searchable {

    companion object {
        private const val REQUEST_CODE_IMPORT = 666
    }

    lateinit var binding: DbinspectorActivityDatabasesBinding

    private val viewModel: DatabaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DbinspectorActivityDatabasesBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupToolbar()
        setupSwipeRefresh()
        setupRecyclerView()

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
                    data?.clipData?.let { clipData ->
                        importDatabases((0 until clipData.itemCount).map { clipData.getItemAt(it).uri })
                    } ?: data?.data?.let {
                        importDatabases(listOf(it))
                    } ?: Unit
                }
                Activity.RESULT_CANCELED -> Unit
                else -> Unit
            }
        }
    }

    override fun onSearchOpened() {
        with(binding.toolbar) {
            menu.findItem(R.id.dbImport).isVisible = false
            menu.findItem(R.id.refresh).isVisible = false
        }
    }

    override fun onSearchClosed() {
        with(binding.toolbar) {
            menu.findItem(R.id.dbImport).isVisible = true
            menu.findItem(R.id.refresh).isVisible = true
        }
    }

    private fun setupToolbar() =
        with(binding.toolbar) {
            navigationIcon = applicationInfo.loadIcon(packageManager)
                .scale(this@DatabasesActivity, R.dimen.dbinspector_app_icon_size, R.dimen.dbinspector_app_icon_size)
            setNavigationOnClickListener { finish() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search -> {
                        onSearchOpened()
                        true
                    }
                    R.id.dbImport -> {
                        importDatabase()
                        true
                    }
                    R.id.refresh -> {
                        refreshDatabases()
                        true
                    }
                    else -> false
                }
            }
            menu.searchView?.setup(
                onSearchClosed = { onSearchClosed() },
                onQueryTextChanged = { search(it) }
            )
        }

    private fun setupSwipeRefresh() =
        with(binding.swipeRefresh) {
            setOnRefreshListener {
                isRefreshing = false
                refreshDatabases()
            }
        }

    private fun setupRecyclerView() =
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(
                this@DatabasesActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        }

    private fun showDatabases(databases: List<Database>) {
        binding.recyclerView.adapter = DatabasesAdapter(
            items = databases,
            onClick = { showTables(it) },
            onDelete = { removeDatabase(it) },
            onRename = { renameDatabase(it) },
            onCopy = { copyDatabase(it) },
            onShare = { shareDatabase(it) }
        )
    }

    private fun showTables(database: Database) =
        startActivity(
            Intent(this, TablesActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE, database)
                }
        )

    private fun refreshDatabases() {
        viewModel.find()
        search(binding.toolbar.menu.searchView?.query?.toString())
    }

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

    private fun importDatabases(uris: List<Uri>) =
        viewModel.import(uris)


    private fun removeDatabase(database: Database) =
        MaterialAlertDialogBuilder(this)
            .setMessage(String.format(getString(R.string.dbinspector_delete_database_confirm), database.name))
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                viewModel.remove("${database.name}.${database.extension}")
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
            .show()

    private fun renameDatabase(database: Database) {
        val filter = InputFilter { source, _, _, _, _, _ ->
            if (source.isEmpty()) return@InputFilter null
            val last = source[source.length - 1]
            val reservedChars = "?:\"*|/\\<>"
            if (reservedChars.indexOf(last) > -1) source.subSequence(0, source.length - 1) else null
        }

        DbinspectorDialogRenameBinding.inflate(layoutInflater)
            .apply {
                nameInput.filters = arrayOf(filter)
                nameInput.setText(database.name)
                nameInput.doOnTextChanged { text, _, _, _ ->
                    if (text.isNullOrBlank()) {
                        inputLayout.error = getString(R.string.dbinspector_rename_database_error_blank)
                    } else {
                        inputLayout.error = null
                    }
                }
            }
            .let {
                MaterialAlertDialogBuilder(this)
                    .setView(it.root)
                    .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                        viewModel.rename(
                            database.absolutePath,
                            "${database.path}/${it.nameInput.text?.toString().orEmpty().trim()}.${database.extension}"
                        )
                        dialog.dismiss()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
    }

    private fun copyDatabase(database: Database) =
        viewModel.copy(
            database.absolutePath,
            database.path,
            database.name,
            database.extension
        )

    private fun shareDatabase(database: Database) =
        try {
            startActivity(
                ShareCompat.IntentBuilder.from(this)
                    .setType("application/octet-stream")
                    .setStream(FileProvider.getUriForFile(this, "${this.packageName}.provider.database", File(database.absolutePath)))
                    .intent.apply {
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
            )
        } catch (exception: ActivityNotFoundException) {
            exception.printStackTrace()
            Toast.makeText(
                this,
                String.format(getString(R.string.dbinspector_share_database_failed), database.name),
                Toast.LENGTH_SHORT
            ).show()
        }

    private fun search(query: String?) {
        (binding.recyclerView.adapter as? DatabasesAdapter)?.filter?.filter(query)
    }
}