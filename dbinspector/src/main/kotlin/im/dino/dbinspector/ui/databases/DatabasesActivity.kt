package im.dino.dbinspector.ui.databases

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorActivityDatabasesBinding
import im.dino.dbinspector.domain.database.models.Database
import im.dino.dbinspector.extensions.scale
import im.dino.dbinspector.extensions.searchView
import im.dino.dbinspector.extensions.setup
import im.dino.dbinspector.ui.pragma.database.EditActivity
import im.dino.dbinspector.ui.schema.SchemaActivity
import im.dino.dbinspector.ui.shared.Constants
import im.dino.dbinspector.ui.shared.Searchable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.io.File

@ExperimentalCoroutinesApi
@FlowPreview
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

        viewModel.browse()

        viewModel.observe {
            refreshDatabases()
        }
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

    override fun search(query: String?) {
        (binding.recyclerView.adapter as? DatabasesAdapter)?.filter?.filter(query)
    }

    override fun searchQuery(): String? =
        binding.toolbar.menu.searchView?.query?.toString()

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
            onClick = { showSchema(it) },
            onDelete = { removeDatabase(it) },
            onEdit = { editDatabase(it) },
            onCopy = { copyDatabase(it) },
            onShare = { shareDatabase(it) }
        )
    }

    private fun showSchema(database: Database) =
        startActivity(
            Intent(this, SchemaActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_PATH, database.absolutePath)
                    putExtra(Constants.Keys.DATABASE_NAME, database.name)
                }
        )

    private fun refreshDatabases() {
        viewModel.browse()
        search(searchQuery())
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

    private fun editDatabase(database: Database) {
        startActivity(
            Intent(this, EditActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_PATH, database.absolutePath)
                    putExtra(Constants.Keys.DATABASE_FILEPATH, database.path)
                    putExtra(Constants.Keys.DATABASE_NAME, database.name)
                    putExtra(Constants.Keys.DATABASE_EXTENSION, database.extension)
                }
        )
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
}