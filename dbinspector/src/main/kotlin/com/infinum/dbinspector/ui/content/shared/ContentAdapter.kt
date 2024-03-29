package com.infinum.dbinspector.ui.content.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.infinum.dbinspector.databinding.DbinspectorItemCellBinding
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.ui.shared.diffutils.CellDiffUtil

internal class ContentAdapter : PagingDataAdapter<Cell, ContentViewHolder>(CellDiffUtil()) {

    var headersCount: Int = 0
    var onCellClicked: ((Cell) -> Unit)? = null

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder =
        ContentViewHolder(
            DbinspectorItemCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        if (headersCount > 0) {
            val item = getItem(position)
            holder.bind(item, position / headersCount, onCellClicked)
        }
    }

    override fun onViewRecycled(holder: ContentViewHolder) =
        with(holder) {
            unbind()
            super.onViewRecycled(this)
        }
}
