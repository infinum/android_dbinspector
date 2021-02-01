package com.infinum.dbinspector.ui.databases.edit

import android.content.Context
import android.content.Intent
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.shared.base.BaseContract

internal class EditDatabaseContract : BaseContract<EditDatabaseContract.Input>() {

    override fun createIntent(context: Context, input: Input): Intent =
        Intent(context, EditDatabaseActivity::class.java)
            .apply {
                putExtra(Presentation.Constants.Keys.DATABASE_PATH, input.absolutePath)
                putExtra(Presentation.Constants.Keys.DATABASE_FILEPATH, input.parentPath)
                putExtra(Presentation.Constants.Keys.DATABASE_NAME, input.name)
                putExtra(Presentation.Constants.Keys.DATABASE_EXTENSION, input.extension)
            }

    data class Input(
        val absolutePath: String,
        val parentPath: String,
        val name: String,
        val extension: String
    ) : BaseContract.Input
}
