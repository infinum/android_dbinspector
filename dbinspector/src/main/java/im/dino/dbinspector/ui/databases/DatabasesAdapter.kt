package im.dino.dbinspector.ui.databases

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.databinding.DbinspectorItemDatabaseBinding
import im.dino.dbinspector.domain.database.models.Database

class DatabasesAdapter(
    private val items: List<Database> = listOf(),
    private val onClick: (Database) -> Unit
) : RecyclerView.Adapter<DatabaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatabaseViewHolder =
        DatabaseViewHolder(DbinspectorItemDatabaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DatabaseViewHolder, position: Int) =
        holder.bind(items[position], onClick)

    override fun onViewRecycled(holder: DatabaseViewHolder) =
        with(holder) {
            unbind()
            super.onViewRecycled(this)
        }

    override fun getItemCount(): Int = items.size
}