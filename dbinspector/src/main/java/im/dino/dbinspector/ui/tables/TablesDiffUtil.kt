package im.dino.dbinspector.ui.tables

import androidx.recyclerview.widget.DiffUtil
import im.dino.dbinspector.domain.table.models.Table

class TablesDiffUtil : DiffUtil.ItemCallback<Table>() {

    override fun areItemsTheSame(oldItem: Table, newItem: Table): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Table, newItem: Table): Boolean {
        return oldItem.name == newItem.name
    }
}