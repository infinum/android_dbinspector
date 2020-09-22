package im.dino.dbinspector.ui.databases

import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.R
import im.dino.dbinspector.databinding.DbinspectorItemDatabaseBinding
import im.dino.dbinspector.domain.database.models.Database

class DatabaseViewHolder(
    private val viewBinding: DbinspectorItemDatabaseBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(
        item: Database,
        onClick: (Database) -> Unit,
        onDelete: (Database) -> Unit,
        onRename: (Database) -> Unit,
        onCopy: (Database) -> Unit,
        onShare: (Database) -> Unit
    ) {
        with(viewBinding) {
            this.path.text = item.path
            this.name.text = item.name
            this.version.text = item.version
            this.toolbar.setNavigationOnClickListener { onDelete(item) }
            this.toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.rename -> onRename(item)
                    R.id.copy -> onCopy(item)
                    R.id.share -> onShare(item)
                }
                true
            }
            this.content.setOnClickListener { onClick(item) }
        }
    }

    fun unbind() {
        with(viewBinding) {
            this.toolbar.setNavigationOnClickListener(null)
            this.toolbar.setOnMenuItemClickListener(null)
            this.content.setOnClickListener(null)
        }
    }
}