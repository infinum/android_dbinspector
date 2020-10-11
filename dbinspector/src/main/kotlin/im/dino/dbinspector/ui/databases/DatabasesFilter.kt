package im.dino.dbinspector.ui.databases

import android.widget.Filter
import im.dino.dbinspector.domain.database.models.DatabaseDescriptor
import java.util.Locale

@Deprecated("Replace by Kotlin chain")
internal class DatabasesFilter(
    private val items: List<DatabaseDescriptor>,
    private val onFiltered: (List<DatabaseDescriptor>) -> Unit
) : Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults =
        FilterResults().apply {
            values = if (constraint.isNullOrBlank()) {
                items
            } else {
                items.filter {
                    it.name
                        .toLowerCase(Locale.getDefault())
                        .contains(constraint.toString().toLowerCase(Locale.getDefault()))
                }
            }
        }

    @Suppress("UNCHECKED_CAST")
    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        results?.let {
            onFiltered(it.values as List<DatabaseDescriptor>)
        }
    }
}
