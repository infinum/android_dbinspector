package com.infinum.dbinspector.ui.databases.edit

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivityEditBinding
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.ui.shared.Constants
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class EditActivity : BaseActivity() {

    override val binding by viewBinding(DbinspectorActivityEditBinding::inflate)

    private val viewModel: EditViewModel by viewModel()

    private var databasePath: String? = null
    private var databaseFilepath: String? = null
    private var databaseName: String? = null
    private var databaseExtension: String? = null

    private val nameFilter = InputFilter { source, _, _, _, _, _ ->
        if (source.isEmpty()) return@InputFilter null
        val last = source[source.length - 1]
        val reservedChars = "?:\"*|/\\<>"
        if (reservedChars.indexOf(last) > -1) source.subSequence(0, source.length - 1) else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
        }

        intent.extras?.let {
            databasePath = it.getString(Constants.Keys.DATABASE_PATH)
            databaseFilepath = it.getString(Constants.Keys.DATABASE_FILEPATH)
            databaseName = it.getString(Constants.Keys.DATABASE_NAME)
            databaseExtension = it.getString(Constants.Keys.DATABASE_EXTENSION)
            if (
                listOf(
                    databasePath,
                    databaseFilepath,
                    databaseName,
                    databaseExtension
                ).none { parameter -> parameter.isNullOrBlank() }
            ) {
                setupUi()
            } else {
                showError()
            }
        } ?: showError()
    }

    private fun setupUi() {
        with(binding) {
            toolbar.subtitle = databaseName

            nameInput.filters = arrayOf(nameFilter)
            nameInput.setText(databaseName)
            nameInput.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrBlank()) {
                    nameInputLayout.error = getString(R.string.dbinspector_rename_database_error_blank)
                } else {
                    nameInputLayout.error = null
                }
            }
            nameInput.setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    rename()
                    view.clearFocus()
                }
                false
            }
        }
    }

    private fun showError() =
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(R.string.dbinspector_title_error)
            .setMessage(R.string.dbinspector_error_parameters)
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                finish()
            }
            .create()
            .show()

    private fun rename() {
        databasePath?.let {
            val newDatabaseName = binding.nameInput.text?.toString().orEmpty().trim()
            viewModel.rename(
                database = DatabaseDescriptor(
                    exists = true,
                    parentPath = databaseFilepath.orEmpty(),
                    name = databaseName.orEmpty(),
                    extension = databaseExtension.orEmpty()
                ),
                newName = newDatabaseName
            ) {
                this.databasePath = it.absolutePath
                this.databaseName = it.name
                setResult(
                    Activity.RESULT_OK,
                    Intent().apply {
                        putExtra(Constants.Keys.SHOULD_REFRESH, true)
                    }
                )
            }
        } ?: showError()
    }
}
