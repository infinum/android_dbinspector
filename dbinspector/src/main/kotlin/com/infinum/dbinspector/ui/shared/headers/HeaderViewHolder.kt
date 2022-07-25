package com.infinum.dbinspector.ui.shared.headers

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.databinding.DbinspectorItemHeaderBinding
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.ui.extensions.drawableFromAttribute

internal class HeaderViewHolder(
    private val viewBinding: DbinspectorItemHeaderBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(
        item: Header,
        isClickable: Boolean,
        onClick: ((Header) -> Unit)?
    ) {
        with(viewBinding) {
            val nextSort = if (item.sort == Sort.ASCENDING) {
                Sort.DESCENDING
            } else {
                Sort.ASCENDING
            }

            this.valueView.background = if (isClickable) {
                this.valueView.context.drawableFromAttribute(android.R.attr.selectableItemBackground)
            } else {
                null
            }
            this.valueView.text = item.name
            this.valueView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                if (item.active) ContextCompat.getDrawable(this.valueView.context, nextSort.icon) else null,
                null
            )
            this.valueView.isClickable = isClickable
            this.valueView.setOnClickListener {
                onClick?.invoke(
                    item.copy(sort = nextSort)
                )
            }
        }
    }

    fun unbind() {
        with(viewBinding) {
            this.valueView.background = null
            this.valueView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
            this.valueView.isClickable = false
            this.valueView.setOnClickListener(null)
        }
    }
}
