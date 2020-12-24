package com.infinum.dbinspector.ui.content.shared

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.text.format.Formatter
import android.view.LayoutInflater
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorLayoutImagePreviewBinding
import com.infinum.dbinspector.databinding.DbinspectorLayoutTextPreviewBinding
import java.io.File
import java.io.FileOutputStream

internal class ContentPreviewFactory(
    private val activity: Activity
) {

    fun showText(text: String) =
        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.dbinspector_title_preview)
            .setView(
                DbinspectorLayoutTextPreviewBinding.inflate(LayoutInflater.from(activity))
                    .apply {
                        previewView.text = text
                    }
                    .root
            )
            .setPositiveButton(R.string.dbinspector_action_share) { dialog: DialogInterface, _ ->
                dialog.dismiss()
                activity.startActivity(
                    ShareCompat.IntentBuilder.from(activity)
                        .setType("text/plain")
                        .setText(text)
                        .intent
                )
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _ ->
                dialog.dismiss()
            }
            .create()
            .show()

    @SuppressLint("SetTextI18n")
    fun showImage(imageBytes: ByteArray, suffix: String) =
        BitmapFactory.decodeByteArray(
            imageBytes,
            0,
            imageBytes.size,
            BitmapFactory.Options().apply { inMutable = true }
        ).let { image ->
            MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.dbinspector_title_preview)
                .setView(
                    DbinspectorLayoutImagePreviewBinding.inflate(LayoutInflater.from(activity))
                        .apply {
                            previewView.setImageBitmap(image)
                            descriptionView.text = "${image.width} x ${image.height} - " +
                                Formatter.formatShortFileSize(descriptionView.context, imageBytes.size.toLong())
                        }
                        .root
                )
                .setPositiveButton(R.string.dbinspector_action_share) { dialog: DialogInterface, _ ->
                    dialog.dismiss()
                    activity.startActivity(
                        ShareCompat.IntentBuilder.from(activity)
                            .setType("image/*")
                            .setStream(
                                File.createTempFile("dbinspector_", suffix).let { file ->
                                    FileOutputStream(file).use { output ->
                                        output.write(imageBytes)
                                        output.flush()
                                    }
                                    FileProvider.getUriForFile(
                                        activity,
                                        "${activity.packageName}.com.infinum.dbinspector.provider.database",
                                        file
                                    )
                                }
                            )
                            .intent
                    )
                }
                .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
}
