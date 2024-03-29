package com.infinum.dbinspector.ui.databases

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.infinum.dbinspector.R
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.schema.SchemaActivity
import com.infinum.dbinspector.ui.settings.SettingsActivity
import java.io.File

internal class NavigatorIntentFactory(
    private val context: Context
) {

    companion object {

        private val SUPPORTED_MIME_TYPE = setOf(
            "application/*",
            "application/x-sqlite3",
            "application/vnd.sqlite3"
        )
    }

    fun showSettings() =
        context.startActivity(
            Intent(context, SettingsActivity::class.java)
        )

    fun showSchema(database: DatabaseDescriptor) =
        context.startActivity(
            Intent(context, SchemaActivity::class.java)
                .apply {
                    putExtra(Presentation.Constants.Keys.DATABASE_PATH, database.absolutePath)
                    putExtra(Presentation.Constants.Keys.DATABASE_NAME, database.name)
                }
        )

    fun showImportChooser(): Intent =
        Intent.createChooser(
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                type = SUPPORTED_MIME_TYPE.first()
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            },
            context.getString(R.string.dbinspector_action_import)
        )

    @Suppress("SwallowedException", "ThrowingExceptionsWithoutMessageOrCause")
    fun showShare(database: DatabaseDescriptor) =
        try {
            (context as? Activity)?.let {
                context.startActivity(
                    ShareCompat.IntentBuilder(it)
                        .setType("application/octet-stream")
                        .setStream(
                            FileProvider.getUriForFile(
                                it,
                                "${it.packageName}.com.infinum.dbinspector.provider.database",
                                File(database.absolutePath)
                            )
                        )
                        .intent.apply {
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                )
            } ?: throw NullPointerException()
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(
                context,
                String.format(context.getString(R.string.dbinspector_share_database_failed), database.name),
                Toast.LENGTH_SHORT
            ).show()
        }
}
