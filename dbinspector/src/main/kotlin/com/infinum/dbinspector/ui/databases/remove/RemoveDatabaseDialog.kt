package com.infinum.dbinspector.ui.databases.remove

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorDialogRemoveDatabaseBinding
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.REMOVE_DATABASE
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.REMOVE_DATABASE_DESCRIPTOR
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.SHOULD_REFRESH
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.base.BaseBottomSheetDialogFragment
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import getParcelableCompat
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class RemoveDatabaseDialog :
    BaseBottomSheetDialogFragment<RemoveDatabaseState, Any>(R.layout.dbinspector_dialog_remove_database) {

    companion object {
        fun withDatabaseDescriptor(database: DatabaseDescriptor): RemoveDatabaseDialog {
            val fragment = RemoveDatabaseDialog()
            fragment.arguments = Bundle().apply {
                putParcelable(REMOVE_DATABASE_DESCRIPTOR, database)
            }
            return fragment
        }
    }

    override val binding: DbinspectorDialogRemoveDatabaseBinding by viewBinding(
        DbinspectorDialogRemoveDatabaseBinding::bind
    )

    override val viewModel: RemoveDatabaseViewModel by viewModel()

    private var database: DatabaseDescriptor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = arguments?.getParcelableCompat(
            REMOVE_DATABASE_DESCRIPTOR,
            DatabaseDescriptor::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { dismiss() }
            messageView.text = String.format(
                getString(R.string.dbinspector_delete_database_confirm),
                database?.name.orEmpty()
            )
            removeButton.setOnClickListener { view ->
                database?.let {
                    viewModel.remove(view.context, it)
                } ?: (activity as? BaseActivity<*, *>)?.showDatabaseParametersError()
            }
        }
    }

    override fun onState(state: RemoveDatabaseState) =
        when (state) {
            is RemoveDatabaseState.Removed -> {
                setFragmentResult(
                    REMOVE_DATABASE,
                    bundleOf(
                        SHOULD_REFRESH to state.success
                    )
                )
                dismiss()
            }
        }

    override fun onEvent(event: Any) = Unit
}
