package com.infinum.dbinspector.ui.content.shared.preview

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.format.Formatter
import android.view.View
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorDialogPreviewContentBinding
import com.infinum.dbinspector.domain.schema.shared.models.ImageType
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.extensions.toChecksum
import com.infinum.dbinspector.extensions.toUtf8String
import com.infinum.dbinspector.ui.Presentation.Constants.Keys.PREVIEW_CELL
import com.infinum.dbinspector.ui.shared.base.BaseBottomSheetDialogFragment
import com.infinum.dbinspector.ui.shared.base.BaseViewModel
import com.infinum.dbinspector.ui.shared.delegates.viewBinding
import java.io.File
import java.io.FileOutputStream

internal class PreviewContentDialog :
    BaseBottomSheetDialogFragment<Any, Any>(R.layout.dbinspector_dialog_preview_content) {

    companion object {
        fun setCell(cell: Cell): PreviewContentDialog {
            val fragment = PreviewContentDialog()
            fragment.arguments = Bundle().apply {
                putParcelable(PREVIEW_CELL, cell)
            }
            return fragment
        }
    }

    override val binding: DbinspectorDialogPreviewContentBinding by viewBinding(
        DbinspectorDialogPreviewContentBinding::bind
    )

    override val viewModel: BaseViewModel<Any, Any>? = null

    private var cell: Cell? = null
    private var cellText: String? = null
    private var cellData: ByteArray? = null
    private var cellImageTypeSuffix: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            cell = it.getParcelable(PREVIEW_CELL)
        }
    }

    @Suppress("NestedBlockDepth")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { dismiss() }
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.copy -> {
                        copy()
                        true
                    }
                    R.id.share -> {
                        share()
                        true
                    }
                    else -> false
                }
            }
            cell?.let { cell ->
                cell.data?.let { bytes ->
                    when (cell.imageType) {
                        ImageType.UNSUPPORTED -> cell.text?.let {
                            cellText = it
                            showText()
                        }
                        else -> {
                            cellData = bytes
                            cellImageTypeSuffix = cell.imageType.suffix
                            showImage()
                        }
                    }
                } ?: cell.text?.let {
                    cellText = it
                    showText()
                }
            } ?: dismiss()
        }
    }

    override fun onState(state: Any) = Unit

    override fun onEvent(event: Any) = Unit

    private fun showText() {
        with(binding) {
            messageScrollView.isVisible = true
            imageLayout.isVisible = false
            messageView.text = cellText
        }
    }

    private fun copy() {
        cellData?.let {
            copyToClipboard(it.toUtf8String())
        } ?: cellText?.let { copyToClipboard(it) }
    }

    private fun share() {
        cellData?.let { imageBytes ->
            startActivity(
                ShareCompat.IntentBuilder(requireContext())
                    .setType("image/*")
                    .setStream(
                        File(
                            requireContext().cacheDir,
                            "dbinspector_${imageBytes.toChecksum()}${cellImageTypeSuffix.orEmpty()}"
                        )
                            .also {
                                if (it.exists().not()) {
                                    FileOutputStream(it).use { output ->
                                        output.write(imageBytes)
                                        output.flush()
                                    }
                                }
                            }.let {
                                FileProvider.getUriForFile(
                                    requireContext(),
                                    "${requireContext().packageName}.com.infinum.dbinspector.provider.database",
                                    it
                                )
                            }
                    )
                    .intent
            )
        } ?: cellText?.let {
            startActivity(
                ShareCompat.IntentBuilder(requireContext())
                    .setType("text/plain")
                    .setText(it)
                    .intent
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showImage() {
        cellData?.let { imageBytes ->
            BitmapFactory.decodeByteArray(
                imageBytes,
                0,
                imageBytes.size,
                BitmapFactory.Options().apply { inMutable = true }
            ).let { image ->
                with(binding) {
                    messageScrollView.isVisible = false
                    imageLayout.isVisible = true
                    imageView.setImageBitmap(image)
                    descriptionView.text = "${image.width} x ${image.height} " +
                        Formatter.formatShortFileSize(descriptionView.context, imageBytes.size.toLong())
                }
            }
        }
    }

    private fun copyToClipboard(content: String) =
        (activity?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.let {
            it.setPrimaryClip(
                ClipData.newPlainText(
                    getString(R.string.dbinspector_launcher_name),
                    content
                )
            )
            Toast.makeText(activity, R.string.dbinspector_preview_success, Toast.LENGTH_SHORT).show()
        }
            ?: Toast.makeText(activity, R.string.dbinspector_preview_failed, Toast.LENGTH_SHORT).show()
}
