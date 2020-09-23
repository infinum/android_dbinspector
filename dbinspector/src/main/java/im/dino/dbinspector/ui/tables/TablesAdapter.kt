package im.dino.dbinspector.ui.tables

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import im.dino.dbinspector.databinding.DbinspectorItemTableBinding

class TablesAdapter(
    private val onClick: (String) -> Unit
) : PagingDataAdapter<String, TableViewHolder>(TablesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder =
        TableViewHolder(DbinspectorItemTableBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) =
        holder.bind(getItem(position), onClick)

    override fun onViewRecycled(holder: TableViewHolder) =
        with(holder) {
            unbind()
            super.onViewRecycled(this)
        }
}