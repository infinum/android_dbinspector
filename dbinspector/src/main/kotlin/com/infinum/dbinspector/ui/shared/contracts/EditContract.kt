package com.infinum.dbinspector.ui.shared.contracts

import android.content.Context
import android.content.Intent
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.edit.EditActivity
import com.infinum.dbinspector.ui.shared.base.BaseContract

internal class EditContract : BaseContract<EditContract.Input>() {

    override fun createIntent(context: Context, input: Input): Intent =
        Intent(context, EditActivity::class.java)
            .apply {
                putExtra(Presentation.Constants.Keys.DATABASE_PATH, input.databasePath)
                putExtra(Presentation.Constants.Keys.DATABASE_NAME, input.databaseName)
            }

    data class Input(
        val databaseName: String,
        val databasePath: String
    ) : BaseContract.Input
}
