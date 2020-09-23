package im.dino.dbinspector.ui.databases

import android.widget.Filter
import im.dino.dbinspector.domain.database.models.Database
import java.util.Locale

class DatabasesFilter(
    private val items: List<Database>,
    private val onFiltered: (List<Database>) -> Unit
) : Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults =
        FilterResults().apply {
            values = if (constraint.isNullOrBlank()) {
                items
            } else {
                items.filter { it.name.toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault())) }
            }
        }

    @Suppress("UNCHECKED_CAST")
    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        results?.let {
            onFiltered(it.values as List<Database>)
        }
    }
}