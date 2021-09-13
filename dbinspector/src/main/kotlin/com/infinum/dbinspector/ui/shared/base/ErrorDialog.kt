package com.infinum.dbinspector.ui.shared.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorDialogErrorBinding
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.ERROR_MESSAGE
import com.infinum.dbinspector.ui.shared.delegates.viewBinding

internal class ErrorDialog : BaseBottomSheetDialogFragment<Any, Any>(R.layout.dbinspector_dialog_error) {

    companion object {
        fun setMessage(message: String): ErrorDialog {
            val fragment = ErrorDialog()
            fragment.arguments = Bundle().apply {
                putString(ERROR_MESSAGE, message)
            }
            return fragment
        }
    }

    override val binding: DbinspectorDialogErrorBinding by viewBinding(
        DbinspectorDialogErrorBinding::bind
    )

    override val viewModel: BaseViewModel<Any, Any>? = null

    private var message: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            isCancelable = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            message = it.getString(ERROR_MESSAGE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            messageView.text = message
            dismissButton.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    override fun onState(state: Any) = Unit

    override fun onEvent(event: Any) = Unit
}
