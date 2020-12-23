package com.infinum.dbinspector.ui.content.shared

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.text.format.Formatter
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorLayoutImagePreviewBinding

internal class ContentPreviewFactory(
    private val context: Context
) {

    @SuppressLint("SetTextI18n")
    fun showImagePreview(image: Bitmap, width: Int, height: Int, size: Long) =
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.dbinspector_title_preview)
            .setView(
                DbinspectorLayoutImagePreviewBinding.inflate(LayoutInflater.from(context))
                    .apply {
                        previewView.setImageBitmap(image)
                        descriptionView.text = "$width x $height - " +
                            Formatter.formatShortFileSize(descriptionView.context, size)
                    }
                    .root
            )
            .setPositiveButton(R.string.dbinspector_action_close) { dialog: DialogInterface, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
}
