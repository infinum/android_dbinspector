package com.infinum.dbinspector.ui.content.shared

import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorItemCellBinding
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.TruncateMode

internal class ContentViewHolder(
    private val viewBinding: DbinspectorItemCellBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(
        item: Cell?,
        row: Int,
        onCellClicked: (Cell) -> Unit
    ) {
        item?.let { cell ->
            bindValue(cell)
            bindRoot(row, cell, onCellClicked)
        } ?: bindNullValue()
    }

    fun unbind() {
        with(viewBinding) {
            this.valueView.maxLines = Int.MAX_VALUE
            this.valueView.ellipsize = null
            this.truncatedIndicator.isVisible = false
            this.root.setBackgroundColor(ContextCompat.getColor(this.root.context, android.R.color.transparent))
            this.root.setOnClickListener(null)
        }
    }

    private fun bindRoot(
        row: Int,
        cell: Cell,
        onCellClicked: (Cell) -> Unit
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
            this.root.setOnClickListener {
                onCellClicked(cell)
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
            this.truncatedIndicator.isVisible = cell.linesShown != Int.MAX_VALUE && cell.data != null
        }

    private fun bindNullValue() =
        with(viewBinding) {
            this.valueView.maxLines = Int.MAX_VALUE
            this.valueView.ellipsize = null
            this.valueView.text = null
            this.truncatedIndicator.isVisible = false
            this.root.background = ContextCompat.getDrawable(this.root.context, R.drawable.dbinspector_placeholder)
            this.root.setOnClickListener(null)
        }
}
