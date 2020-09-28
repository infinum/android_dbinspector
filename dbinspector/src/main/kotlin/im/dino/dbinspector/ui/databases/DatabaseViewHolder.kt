package im.dino.dbinspector.ui.databases

import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.databinding.DbinspectorItemDatabaseBinding
import im.dino.dbinspector.domain.database.models.Database

internal class DatabaseViewHolder(
    private val viewBinding: DbinspectorItemDatabaseBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(
        item: Database,
        onClick: (Database) -> Unit,
        onDelete: (Database) -> Unit,
        onEdit: (Database) -> Unit,
        onCopy: (Database) -> Unit,
        onShare: (Database) -> Unit
    ) {
        with(viewBinding) {
            this.path.text = item.path
            this.name.text = item.name
            this.version.text = item.version
            this.removeButton.setOnClickListener { onDelete(item) }
            this.editButton.setOnClickListener { onEdit(item) }
            this.copyButton.setOnClickListener { onCopy(item) }
            this.shareButton.setOnClickListener { onShare(item) }
            this.content.setOnClickListener { onClick(item) }
        }
    }

    fun unbind() {
        with(viewBinding) {
            this.removeButton.setOnClickListener(null)
            this.editButton.setOnClickListener(null)
            this.copyButton.setOnClickListener(null)
            this.shareButton.setOnClickListener(null)
            this.content.setOnClickListener(null)
        }
    }
}