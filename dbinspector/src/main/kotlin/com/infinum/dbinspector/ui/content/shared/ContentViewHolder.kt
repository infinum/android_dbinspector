package com.infinum.dbinspector.ui.content.shared

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorItemCellBinding
import com.infinum.dbinspector.domain.schema.shared.models.ImageType
import com.infinum.dbinspector.domain.shared.models.Cell

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
            with(viewBinding) {
                this.valueView.maxLines = cell.linesShown
                this.valueView.ellipsize = cell.truncateMode.takeIf { cell.linesShown == Int.MAX_VALUE }
                this.valueView.text = cell.text
                this.root.setBackgroundColor(
                    if (row % 2 == 0) {
                        ContextCompat.getColor(this.root.context, R.color.dbinspector_alternate_row_background)
                    } else {
                        ContextCompat.getColor(this.root.context, android.R.color.transparent)
                    }
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
        } ?: with(viewBinding) {
            this.valueView.maxLines = Int.MAX_VALUE
            this.valueView.ellipsize = null
            this.valueView.text = null
            this.root.background = ContextCompat.getDrawable(this.root.context, R.drawable.dbinspector_placeholder)
            this.root.setOnClickListener(null)
        }
    }

    fun unbind() {
        with(viewBinding) {
            this.valueView.maxLines = Int.MAX_VALUE
            this.valueView.ellipsize = null
            this.root.setBackgroundColor(ContextCompat.getColor(this.root.context, android.R.color.transparent))
            this.root.setOnClickListener(null)
        }
    }
}
