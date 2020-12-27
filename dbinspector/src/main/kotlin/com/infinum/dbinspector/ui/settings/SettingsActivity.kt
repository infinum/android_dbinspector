package com.infinum.dbinspector.ui.settings

import android.os.Bundle
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorActivitySettingsBinding
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import kotlin.math.roundToInt

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
            linesView.isInvisible = false
            linesView.text = "100"
            linesSlider.valueFrom = 1.0f
            linesSlider.valueTo = 100.0f
            linesSlider.value = 100.0f
            linesSlider.addOnChangeListener { _, value, _ ->
                linesView.text = value.roundToInt().toString()
            }
            linesCheckBox.setOnCheckedChangeListener { _, isChecked ->
                linesView.isInvisible = isChecked.not()
                linesLayout.isVisible = isChecked
            }
            decreaseLinesButton.setOnClickListener {
                linesSlider.value = (linesSlider.value - 1).coerceAtLeast(1.0f)
            }
            increaseLinesButton.setOnClickListener {
                linesSlider.value = (linesSlider.value + 1).coerceAtMost(100.0f)
            }
            truncateGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {

                }
            }

            placeHolderButton.isEnabled = true
            utf8Button.isEnabled = true
            hexadecimalButton.isEnabled = true
            base64Button.isEnabled = true
            blobPreviewGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.placeHolderButton -> Unit
                    R.id.utf8Button -> Unit
                    R.id.hexadecimalButton -> Unit
                    R.id.base64Button -> Unit
                }
            }
        }
    }
}
