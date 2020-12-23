package com.infinum.dbinspector.ui.content.shared

import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorItemCellBinding
import com.infinum.dbinspector.domain.schema.shared.models.ImageType
import com.infinum.dbinspector.domain.shared.models.Cell

internal class ContentViewHolder(
    private val viewBinding: DbinspectorItemCellBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: Cell?, row: Int, onImagePreview: (ByteArray) -> Unit) {
        item?.let {
            with(viewBinding) {
                this.valueView.text = it.text
                this.root.setBackgroundColor(
                    if (row % 2 == 0) {
                        ContextCompat.getColor(this.root.context, R.color.dbinspector_alternate_row_background)
                    } else {
                        ContextCompat.getColor(this.root.context, android.R.color.transparent)
                    }
                )
                this.expandCollapseButton.isVisible = it.isExpandable
                if (it.isExpanded) {
                    // set it to wrap content?
                } else {
                    // set it to exact height of 40dp? but it needs to fit the button?
                }
                this.root.setOnLongClickListener { _ ->
                    when (it.imageType) {
                        ImageType.UNSUPPORTED -> it.data?.let { onImagePreview(it) }
                        else -> it.data?.let { onImagePreview(it) }
                    }
                    true
                }
            }
        } ?: with(viewBinding) {
            this.valueView.text = null
            this.root.background = ContextCompat.getDrawable(this.root.context, R.drawable.dbinspector_placeholder)
            this.root.setOnLongClickListener(null)
        }
    }

    fun unbind() {
        with(viewBinding) {
            this.expandCollapseButton.isGone = true
            this.root.setBackgroundColor(ContextCompat.getColor(this.root.context, android.R.color.transparent))
        }
    }
}
