package im.dino.dbinspector.ui.tables

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import im.dino.dbinspector.databinding.DbinspectorItemTableBinding
import im.dino.dbinspector.domain.table.models.Table

class TablesAdapter(
    private val onClick: (Table) -> Unit,
    private val onLongClick: (Table) -> Boolean
) : PagingDataAdapter<Table, TableViewHolder>(TablesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder =
        TableViewHolder(DbinspectorItemTableBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) =
        holder.bind(getItem(position), onClick, onLongClick)
}