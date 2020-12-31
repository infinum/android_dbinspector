package com.infinum.dbinspector.ui.schema.shared

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.infinum.dbinspector.ui.Presentation

class SchemaContract : ActivityResultContract<SchemaContract.Input, Boolean>() {

    override fun createIntent(context: Context, input: Input): Intent =
        Intent(context, input.childView)
            .apply {
                putExtra(Presentation.Constants.Keys.DATABASE_NAME, input.databaseName)
                putExtra(Presentation.Constants.Keys.DATABASE_PATH, input.databasePath)
                putExtra(Presentation.Constants.Keys.SCHEMA_NAME, input.schemaName)
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
        val childView: Class<*>,
        val databaseName: String,
        val databasePath: String,
        val schemaName: String
    )
}
