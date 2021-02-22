package com.infinum.dbinspector.ui.edit.history

import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R
import com.infinum.dbinspector.databinding.DbinspectorItemExecutionBinding
import com.infinum.dbinspector.domain.history.models.Execution
import java.text.SimpleDateFormat
import java.util.Date

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
            this.successView.setImageResource(
                if (item.isSuccessful) {
                    R.drawable.dbinspector_ic_success
                } else {
                    R.drawable.dbinspector_ic_fail
                }
            )
            this.timeView.text = SimpleDateFormat.getDateTimeInstance().format(Date(item.timestamp))
            this.valueView.text = item.statement
        }

    private fun bindNullValue() =
        with(viewBinding) {
            this.successView.setImageDrawable(null)
            this.timeView.text = null
            this.valueView.text = null
            this.root.setOnClickListener(null)
        }
}
