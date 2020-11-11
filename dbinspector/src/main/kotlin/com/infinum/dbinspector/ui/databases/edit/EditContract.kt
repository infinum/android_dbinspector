package com.infinum.dbinspector.ui.databases.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.infinum.dbinspector.ui.shared.Constants

class EditContract : ActivityResultContract<EditContract.Input, Boolean>() {

    override fun createIntent(context: Context, input: Input): Intent =
        Intent(context, EditActivity::class.java)
            .apply {
                putExtra(Constants.Keys.DATABASE_PATH, input.absolutePath)
                putExtra(Constants.Keys.DATABASE_FILEPATH, input.parentPath)
                putExtra(Constants.Keys.DATABASE_NAME, input.name)
                putExtra(Constants.Keys.DATABASE_EXTENSION, input.extension)
            }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return if (resultCode == Activity.RESULT_OK) {
            intent?.getBooleanExtra(Constants.Keys.SHOULD_REFRESH, false) ?: false
        } else {
            false
        }
    }

    data class Input(
        val absolutePath: String,
        val parentPath: String,
        val name: String,
        val extension: String
    )
}
