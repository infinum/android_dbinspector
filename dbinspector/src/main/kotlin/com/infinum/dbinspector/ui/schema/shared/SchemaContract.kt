package com.infinum.dbinspector.ui.schema.shared

import android.content.Context
import android.content.Intent
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.shared.base.BaseContract

internal class SchemaContract : BaseContract<SchemaContract.Input>() {

    override fun createIntent(context: Context, input: Input): Intent =
        Intent(context, input.childView)
            .apply {
                putExtra(Presentation.Constants.Keys.DATABASE_NAME, input.databaseName)
                putExtra(Presentation.Constants.Keys.DATABASE_PATH, input.databasePath)
                putExtra(Presentation.Constants.Keys.SCHEMA_NAME, input.schemaName)
            }

    data class Input(
        val childView: Class<*>,
        val databaseName: String,
        val databasePath: String,
        val schemaName: String
    ) : BaseContract.Input
}
