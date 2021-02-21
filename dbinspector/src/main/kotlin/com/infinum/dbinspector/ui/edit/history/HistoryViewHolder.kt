package com.infinum.dbinspector.ui.edit.history

import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.databinding.DbinspectorItemExecutionBinding
import com.infinum.dbinspector.domain.history.models.Execution

internal class HistoryViewHolder(
    private val viewBinding: DbinspectorItemExecutionBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(
        item: Execution?,
        onClicked: (Execution) -> Unit
    ) {
        item?.let {
            bindValue(it)
            bindRoot(it, onClicked)
        } ?: bindNullValue()
    }

    fun unbind() {
        with(viewBinding) {
            this.root.setOnClickListener(null)
        }
    }

    private fun bindRoot(
        item: Execution,
        onClicked: (Execution) -> Unit
    ) =
        with(viewBinding) {
            this.root.setOnClickListener {
                onClicked(item)
            }
        }

    private fun bindValue(item: Execution) =
        with(viewBinding) {
            this.valueView.text = item.statement
        }

    private fun bindNullValue() =
        with(viewBinding) {
            this.valueView.text = null
            this.root.setOnClickListener(null)
        }
}
