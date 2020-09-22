package im.dino.dbinspector.ui.databases

import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.databinding.DbinspectorItemDatabaseBinding
import im.dino.dbinspector.domain.database.models.Database

class DatabaseViewHolder(
    private val viewBinding: DbinspectorItemDatabaseBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(
        item: Database,
        onClick: (Database) -> Unit,
        onDelete: (Database) -> Unit
    ) {
        with(viewBinding) {
            this.path.text = item.path
            this.name.text = item.name
            this.version.text = item.version
            this.toolbar.setNavigationOnClickListener { onDelete(item) }
            this.content.setOnClickListener { onClick(item) }
        }
    }

    fun unbind() {
        with(viewBinding) {
            this.toolbar.setNavigationOnClickListener(null)
            this.content.setOnClickListener(null)
        }
    }
}