package com.infinum.dbinspector.ui.shared.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.infinum.dbinspector.domain.shared.models.Cell

internal class CellDiffUtil : DiffUtil.ItemCallback<Cell>() {

    override fun areItemsTheSame(oldItem: Cell, newItem: Cell): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Cell, newItem: Cell): Boolean {
        return oldItem == newItem
    }
}
