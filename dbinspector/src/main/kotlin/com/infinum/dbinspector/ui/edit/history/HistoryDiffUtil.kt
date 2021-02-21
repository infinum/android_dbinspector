package com.infinum.dbinspector.ui.edit.history

import androidx.recyclerview.widget.DiffUtil
import com.infinum.dbinspector.domain.history.models.Execution

internal class HistoryDiffUtil : DiffUtil.ItemCallback<Execution>() {

    override fun areItemsTheSame(oldItem: Execution, newItem: Execution): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Execution, newItem: Execution): Boolean {
        return oldItem == newItem
    }
}
