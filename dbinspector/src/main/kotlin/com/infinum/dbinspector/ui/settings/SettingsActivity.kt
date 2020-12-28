package com.infinum.dbinspector.ui.settings

import android.os.Bundle
import android.text.TextUtils
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.infinum.dbinspector.R
import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.databinding.DbinspectorActivitySettingsBinding
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.ui.shared.Constants
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

internal class SettingsActivity : BaseActivity() {

    override val binding by viewBinding(DbinspectorActivitySettingsBinding::inflate)

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUi()

        viewModel.load {
            setupUi(it)
        }
    }

    private fun initUi() =
        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }

            linesSlider.valueFrom = Constants.Settings.LINES_LIMIT_MINIMUM.toFloat()
            linesSlider.valueTo = Constants.Settings.LINES_LIMIT_MAXIMUM.toFloat()

            decreaseLinesButton.setOnClickListener {
                linesSlider.value = (linesSlider.value - 1)
                    .coerceAtLeast(Constants.Settings.LINES_LIMIT_MINIMUM.toFloat())
            }
            increaseLinesButton.setOnClickListener {
                linesSlider.value = (linesSlider.value + 1)
                    .coerceAtMost(Constants.Settings.LINES_LIMIT_MAXIMUM.toFloat())
            }
        }

    private fun setupUi(settings: Settings) {
        setupLinesLimit(settings)
        setupBlobPreview(settings)
    }

    private fun setupLinesLimit(settings: Settings) =
        with(binding) {
            linesCheckBox.isEnabled = true
            linesCheckBox.setOnCheckedChangeListener(null)
            linesCheckBox.isChecked = settings.linesLimitEnabled
            linesCheckBox.setOnCheckedChangeListener { _, isChecked ->
                linesView.isInvisible = isChecked.not()
                linesLayout.isVisible = isChecked
                viewModel.toggleLinesLimit(isChecked)
            }

            linesView.isInvisible = settings.linesLimitEnabled.not()
            linesLayout.isVisible = settings.linesLimitEnabled
            linesView.text = settings.linesCount.toString()

            linesSlider.clearOnChangeListeners()
            linesSlider.value = settings.linesCount.toFloat()
            linesSlider.addOnChangeListener { _, value, _ ->
                linesView.text = value.roundToInt().toString()
                viewModel.saveLinesCount(value.roundToInt())
            }

            truncateGroup.clearOnButtonCheckedListeners()
            truncateGroup.check(
                when (settings.truncateMode) {
                    TextUtils.TruncateAt.START -> R.id.truncateStart
                    TextUtils.TruncateAt.MIDDLE -> R.id.truncateMiddle
                    TextUtils.TruncateAt.END -> R.id.truncateEnd
                    else -> R.id.truncateEnd
                }
            )
            truncateGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.truncateStart -> TextUtils.TruncateAt.START
                        R.id.truncateMiddle -> TextUtils.TruncateAt.MIDDLE
                        R.id.truncateEnd -> TextUtils.TruncateAt.END
                        else -> TextUtils.TruncateAt.END
                    }.let {
                        viewModel.saveTruncateMode(it)
                    }
                }
            }
        }

    private fun setupBlobPreview(settings: Settings) =
        with(binding) {
            blobPreviewGroup.setOnCheckedChangeListener(null)
            placeHolderButton.isEnabled = true
            utf8Button.isEnabled = true
            hexadecimalButton.isEnabled = true
            base64Button.isEnabled = true
            blobPreviewGroup.check(
                when (settings.blobPreviewType) {
                    BlobPreviewType.PLACEHOLDER -> R.id.placeHolderButton
                    BlobPreviewType.UTF_8 -> R.id.utf8Button
                    BlobPreviewType.HEX -> R.id.hexadecimalButton
                    BlobPreviewType.BASE_64 -> R.id.base64Button
                    else -> R.id.placeHolderButton
                }
            )
            blobPreviewGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.placeHolderButton -> BlobPreviewType.PLACEHOLDER
                    R.id.utf8Button -> BlobPreviewType.UTF_8
                    R.id.hexadecimalButton -> BlobPreviewType.HEX
                    R.id.base64Button -> BlobPreviewType.BASE_64
                    else -> BlobPreviewType.PLACEHOLDER
                }.let {
                    viewModel.saveBlobPreviewType(it)
                }
            }
        }
}
