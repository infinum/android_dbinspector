package im.dino.dbinspector.ui.tables

import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.databinding.DbinspectorItemTableBinding

internal class TableViewHolder(
    private val viewBinding: DbinspectorItemTableBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: String?, onClick: (String) -> Unit) {
        item?.let { table ->
            with(viewBinding) {
                this.name.text = table
                this.root.setOnClickListener { onClick(table) }
            }
        } ?: with(viewBinding) {
            this.name.text = null
            this.root.setOnClickListener(null)
        }
    }

    fun unbind() {
        with(viewBinding) {
            this.root.setOnClickListener(null)
        }
    }
}