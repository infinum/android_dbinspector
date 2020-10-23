package com.infinum.dbinspector.ui.schema.shared

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.infinum.dbinspector.ui.shared.Constants

class SchemaContract : ActivityResultContract<SchemaContract.Input, Boolean>() {

    override fun createIntent(context: Context, input: Input): Intent =
        Intent(context, input.childView)
            .apply {
                putExtra(Constants.Keys.DATABASE_NAME, input.databaseName)
                putExtra(Constants.Keys.DATABASE_PATH, input.databasePath)
                putExtra(Constants.Keys.SCHEMA_NAME, input.schemaName)
            }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return if (resultCode == Activity.RESULT_OK) {
            intent?.getBooleanExtra(Constants.Keys.SHOULD_REFRESH, false) ?: false
        } else {
            false
        }
    }

    data class Input(
        val childView: Class<*>,
        val databaseName: String,
        val databasePath: String,
        val schemaName: String
    )
}
