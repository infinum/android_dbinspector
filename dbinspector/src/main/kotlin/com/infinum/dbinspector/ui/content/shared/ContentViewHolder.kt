package com.infinum.dbinspector.ui.content.shared

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorItemCellBinding

internal class ContentViewHolder(
    private val viewBinding: DbinspectorItemCellBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: String?, row: Int) {
        item?.let {
            with(viewBinding) {
                this.valueView.text = it
                this.root.setBackgroundColor(
                    if (row % 2 == 0) {
                        ContextCompat.getColor(this.root.context, R.color.dbinspector_alternate_row_background)
                    } else {
                        ContextCompat.getColor(this.root.context, android.R.color.transparent)
                    }
                )
            }
        } ?: with(viewBinding) {
            this.valueView.text = null
            this.root.background = ContextCompat.getDrawable(this.root.context, R.drawable.dbinspector_placeholder)
        }
    }

    fun unbind() {
        with(viewBinding) {
            this.root.setBackgroundColor(ContextCompat.getColor(this.root.context, android.R.color.transparent))
        }
    }
}
