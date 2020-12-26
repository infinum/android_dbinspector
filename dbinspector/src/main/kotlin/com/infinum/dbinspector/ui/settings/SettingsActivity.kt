package com.infinum.dbinspector.ui.settings

import android.os.Bundle
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivitySettingsBinding
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding

internal class SettingsActivity : BaseActivity() {

    override val binding by viewBinding(DbinspectorActivitySettingsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()
    }

    private fun setupUi() {
        binding.toolbar.setNavigationOnClickListener { finish() }

        with(binding) {
            linesCheckBox.isEnabled = true
            linesSlider.valueFrom = 1.0f
            linesSlider.valueTo = 100.0f
            linesSlider.value = 100.0f
            linesCheckBox.setOnCheckedChangeListener { _, isChecked ->
                decreaseLinesButton.isEnabled = isChecked
                linesSlider.isEnabled = isChecked
                increaseLinesButton.isEnabled = isChecked
            }

            blobPreviewGroup.isEnabled = true
            placeHolderButton.isEnabled = true
            utf8Button.isEnabled = true
            hexadecimalButton.isEnabled = true
            base64Button.isEnabled = true
            blobPreviewGroup.setOnCheckedChangeListener { _, checkedId ->
                when(checkedId) {
                    R.id.placeHolderButton -> Unit
                    R.id.utf8Button -> Unit
                    R.id.hexadecimalButton -> Unit
                    R.id.base64Button -> Unit
                }
            }
        }
    }
}
