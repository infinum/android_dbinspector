package com.infinum.dbinspector.ui.edit.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.infinum.dbinspector.databinding.DbinspectorItemExecutionBinding
import com.infinum.dbinspector.domain.history.models.Execution

internal class HistoryAdapter(
    private val onClicked: (Execution) -> Unit
) : ListAdapter<Execution, HistoryViewHolder>(HistoryDiffUtil()) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder =
        HistoryViewHolder(
            DbinspectorItemExecutionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClicked)
    }

    override fun onViewRecycled(holder: HistoryViewHolder) =
        with(holder) {
            unbind()
            super.onViewRecycled(this)
        }
}
