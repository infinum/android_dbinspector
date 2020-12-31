package com.infinum.dbinspector.ui.content.shared

import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorItemCellBinding
import com.infinum.dbinspector.domain.schema.shared.models.ImageType
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.TruncateMode

internal class ContentViewHolder(
    private val viewBinding: DbinspectorItemCellBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(
        item: Cell?,
        row: Int,
        onTextPreview: (String) -> Unit,
        onImagePreview: (ByteArray, String) -> Unit
    ) {
        item?.let { cell ->
            bindValue(cell)
            bindRoot(row, cell, onTextPreview, onImagePreview)
        } ?: bindNullValue()
    }

    fun unbind() {
        with(viewBinding) {
            this.valueView.maxLines = Int.MAX_VALUE
            this.valueView.ellipsize = null
            this.root.setBackgroundColor(ContextCompat.getColor(this.root.context, android.R.color.transparent))
            this.root.setOnClickListener(null)
        }
    }

    private fun bindRoot(
        row: Int,
        cell: Cell,
        onTextPreview: (String) -> Unit,
        onImagePreview: (ByteArray, String) -> Unit
    ) =
        with(viewBinding) {
            this.root.setBackgroundColor(
                ContextCompat.getColor(
                    this.root.context,
                    if (row % 2 == 0) {
                        R.color.dbinspector_alternate_row_background
                    } else {
                        android.R.color.transparent
                    }
                )
            )
            this.root.setOnClickListener { _ ->
                cell.data?.let { bytes ->
                    when (cell.imageType) {
                        ImageType.UNSUPPORTED -> cell.text?.let { onTextPreview(it) }
                        else -> onImagePreview(bytes, cell.imageType.suffix)
                    }
                } ?: cell.text?.let { onTextPreview(it) }
            }
        }

    private fun bindValue(cell: Cell) =
        with(viewBinding) {
            this.valueView.maxLines = cell.linesShown
            this.valueView.ellipsize = cell.truncateMode
                .takeIf { cell.linesShown != Int.MAX_VALUE }
                ?.let {
                    when (it) {
                        TruncateMode.START -> TextUtils.TruncateAt.START
                        TruncateMode.MIDDLE -> TextUtils.TruncateAt.MIDDLE
                        TruncateMode.END -> TextUtils.TruncateAt.END
                    }
                }
            this.valueView.text = cell.text
        }

    private fun bindNullValue() =
        with(viewBinding) {
            this.valueView.maxLines = Int.MAX_VALUE
            this.valueView.ellipsize = null
            this.valueView.text = null
            this.root.background = ContextCompat.getDrawable(this.root.context, R.drawable.dbinspector_placeholder)
            this.root.setOnClickListener(null)
        }
}
