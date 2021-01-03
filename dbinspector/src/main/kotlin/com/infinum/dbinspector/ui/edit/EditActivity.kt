package com.infinum.dbinspector.ui.edit

import android.os.Bundle
import com.infinum.dbinspector.databinding.DbinspectorActivityEditBinding
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class EditActivity : BaseActivity() {

    override val binding by viewBinding(DbinspectorActivityEditBinding::inflate)

    private val viewModel: EditViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }

            editorInput.setText(generate())
        }
    }

    private fun generate(): String {
        val sb = StringBuilder()
        (1 until 11).forEach {
            sb.appendLine("Text $it")
        }
        return sb.toString()
    }
}
