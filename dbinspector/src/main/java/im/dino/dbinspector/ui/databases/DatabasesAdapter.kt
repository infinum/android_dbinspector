package im.dino.dbinspector.ui.databases

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.databinding.DbinspectorItemDatabaseBinding
import im.dino.dbinspector.domain.database.models.Database

class DatabasesAdapter(
    private val items: List<Database> = listOf(),
    private val onClick: (Database) -> Unit,
    private val onDelete: (Database) -> Unit,
    private val onRename: (Database) -> Unit,
    private val onCopy: (Database) -> Unit,
    private val onShare: (Database) -> Unit
) : RecyclerView.Adapter<DatabaseViewHolder>(), Filterable {

    private var filteredItems: List<Database> = listOf()

    init {
        filteredItems = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatabaseViewHolder =
        DatabaseViewHolder(DbinspectorItemDatabaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DatabaseViewHolder, position: Int) =
        holder.bind(
            item = filteredItems[position],
            onClick = onClick,
            onDelete = onDelete,
            onRename = onRename,
            onCopy = onCopy,
            onShare = onShare
        )

    override fun onViewRecycled(holder: DatabaseViewHolder) =
        with(holder) {
            unbind()
            super.onViewRecycled(this)
        }

    override fun getItemCount(): Int = filteredItems.size

    override fun getFilter(): Filter = DatabasesFilter(items) {
        filteredItems = it
        notifyDataSetChanged()
    }
}