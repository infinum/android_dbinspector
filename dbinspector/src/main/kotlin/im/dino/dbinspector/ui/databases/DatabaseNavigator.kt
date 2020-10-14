package im.dino.dbinspector.ui.databases

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import im.dino.dbinspector.R
import im.dino.dbinspector.domain.database.models.DatabaseDescriptor
import im.dino.dbinspector.ui.databases.edit.EditActivity
import im.dino.dbinspector.ui.schema.SchemaActivity
import im.dino.dbinspector.ui.shared.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.io.File

internal class DatabaseNavigator(
    private val context: Context
) {

    companion object {
        const val REQUEST_CODE_IMPORT = 666
    }

    fun showSchema(database: DatabaseDescriptor) =
        context.startActivity(
            Intent(context, SchemaActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_PATH, database.absolutePath)
                    putExtra(Constants.Keys.DATABASE_NAME, database.name)
                }
        )

    fun importDatabase() =
        (context as? Activity)?.let {
            it.startActivityForResult(
                Intent.createChooser(
                    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                    type = "application/vnd.sqlite3"
//                    type = "application/x-sqlite3"
                        type = "application/*"
                        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    },
                    it.getString(R.string.dbinspector_action_import)
                ),
                REQUEST_CODE_IMPORT
            )
        } ?: run {
            Toast.makeText(
                context,
                context.getString(R.string.dbinspector_import_databases_failed),
                Toast.LENGTH_SHORT
            ).show()
        }

    @ExperimentalCoroutinesApi
    fun editDatabase(database: DatabaseDescriptor) {
        context.startActivity(
            Intent(context, EditActivity::class.java)
                .apply {
                    putExtra(Constants.Keys.DATABASE_PATH, database.absolutePath)
                    putExtra(Constants.Keys.DATABASE_FILEPATH, database.parentPath)
                    putExtra(Constants.Keys.DATABASE_NAME, database.name)
                    putExtra(Constants.Keys.DATABASE_EXTENSION, database.extension)
                }
        )
    }

    fun shareDatabase(database: DatabaseDescriptor) =
        try {
            (context as? Activity)?.let {
                context.startActivity(
                    ShareCompat.IntentBuilder.from(it)
                        .setType("application/octet-stream")
                        .setStream(
                            FileProvider.getUriForFile(
                                it,
                                "${it.packageName}.provider.database",
                                File(database.absolutePath)
                            )
                        )
                        .intent.apply {
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                )
            } ?: throw NullPointerException()
        } catch (exception: ActivityNotFoundException) {
            Timber.e(exception)
            Toast.makeText(
                context,
                String.format(context.getString(R.string.dbinspector_share_database_failed), database.name),
                Toast.LENGTH_SHORT
            ).show()
        }
}
