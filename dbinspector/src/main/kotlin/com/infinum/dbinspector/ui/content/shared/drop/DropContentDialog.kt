package com.infinum.dbinspector.ui.content.shared.drop

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorDialogDropContentBinding
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.DROP_CONTENT
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.DROP_MESSAGE
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.DROP_NAME
import com.infinum.dbinspector.ui.shared.base.BaseBottomSheetDialogFragment
import com.infinum.dbinspector.ui.shared.base.BaseViewModel
import com.infinum.dbinspector.ui.shared.delegates.viewBinding

internal class DropContentDialog : BaseBottomSheetDialogFragment<Any, Any>(R.layout.dbinspector_dialog_drop_content) {

    companion object {
        fun setMessage(@StringRes drop: Int, name: String): DropContentDialog {
            val fragment = DropContentDialog()
            fragment.arguments = Bundle().apply {
                putInt(DROP_MESSAGE, drop)
                putString(DROP_NAME, name)
            }
            return fragment
        }
    }

    override val binding: DbinspectorDialogDropContentBinding by viewBinding(
        DbinspectorDialogDropContentBinding::bind
    )

    override val viewModel: BaseViewModel<Any, Any>? = null

    @StringRes
    private var drop: Int? = null

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            drop = it.getInt(DROP_MESSAGE)
            name = it.getString(DROP_NAME)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { dismiss() }
            messageView.text = drop?.let { String.format(getString(it), name.orEmpty()) }
            dropButton.setOnClickListener {
                setFragmentResult(
                    DROP_CONTENT,
                    bundleOf(
                        DROP_NAME to name
                    )
                )
                dismiss()
            }
        }
    }

    override fun onState(state: Any) = Unit

    override fun onEvent(event: Any) = Unit
}
