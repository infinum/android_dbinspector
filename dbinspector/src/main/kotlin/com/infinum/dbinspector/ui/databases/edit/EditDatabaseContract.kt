package com.infinum.dbinspector.ui.databases.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.infinum.dbinspector.ui.Presentation

internal class EditDatabaseContract : ActivityResultContract<EditDatabaseContract.Input, Boolean>() {

    override fun createIntent(context: Context, input: Input): Intent =
        Intent(context, EditDatabaseActivity::class.java)
            .apply {
                putExtra(Presentation.Constants.Keys.DATABASE_PATH, input.absolutePath)
                putExtra(Presentation.Constants.Keys.DATABASE_FILEPATH, input.parentPath)
                putExtra(Presentation.Constants.Keys.DATABASE_NAME, input.name)
                putExtra(Presentation.Constants.Keys.DATABASE_EXTENSION, input.extension)
            }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getBooleanExtra(
                Presentation.Constants.Keys.SHOULD_REFRESH,
                false
            ) ?: false
        } else {
            false
        }

    data class Input(
        val absolutePath: String,
        val parentPath: String,
        val name: String,
        val extension: String
    )
}
