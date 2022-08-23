package com.infinum.dbinspector.ui.databases.rename

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorDialogRenameDatabaseBinding
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.RENAME_DATABASE_DESCRIPTOR
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.base.BaseBottomSheetDialogFragment
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import getParcelableCompat
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class RenameDatabaseDialog :
    BaseBottomSheetDialogFragment<RenameDatabaseState, Any>(R.layout.dbinspector_dialog_rename_database) {

    companion object {
        fun withDatabaseDescriptor(database: DatabaseDescriptor): RenameDatabaseDialog {
            val fragment = RenameDatabaseDialog()
            fragment.arguments = Bundle().apply {
                putParcelable(RENAME_DATABASE_DESCRIPTOR, database)
            }
            return fragment
        }
    }

    override val binding by viewBinding(DbinspectorDialogRenameDatabaseBinding::bind)

    override val viewModel: RenameDatabaseViewModel by viewModel()

    private var database: DatabaseDescriptor? = null

    private val nameFilter = InputFilter { source, _, _, _, _, _ ->
        if (source.isEmpty()) return@InputFilter null
        val last = source[source.length - 1]
        val reservedChars = "?:\"*|/\\<>"
        if (reservedChars.indexOf(last) > -1) source.subSequence(0, source.length - 1) else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = arguments?.getParcelableCompat(
            RENAME_DATABASE_DESCRIPTOR,
            DatabaseDescriptor::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { dismiss() }

            message.text = String.format(
                getString(R.string.dbinspector_rename_database_confirm),
                database?.name.orEmpty()
            )

            nameInput.filters = arrayOf(nameFilter)
            nameInput.setText(database?.name)
            nameInput.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrBlank()) {
                    nameInputLayout.error =
                        getString(R.string.dbinspector_rename_database_error_blank)
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

            renameButton.setOnClickListener {
                rename()
            }
        }
    }

    override fun onState(state: RenameDatabaseState) =
        when (state) {
            is RenameDatabaseState.Renamed -> {
                setFragmentResult(
                    Presentation.Constants.Keys.RENAME_DATABASE,
                    bundleOf(
                        Presentation.Constants.Keys.SHOULD_REFRESH to true
                    )
                )
                dismiss()
            }
        }

    override fun onEvent(event: Any) = Unit

    private fun rename() {
        database?.let {
            val newDatabaseName = binding.nameInput.text?.toString().orEmpty().trim()
            viewModel.rename(
                requireContext(),
                database = it,
                newName = newDatabaseName
            )
        } ?: (activity as? BaseActivity<*, *>)?.showDatabaseParametersError()
    }
}
