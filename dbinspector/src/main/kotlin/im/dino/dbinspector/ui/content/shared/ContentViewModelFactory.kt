package im.dino.dbinspector.ui.content.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import im.dino.dbinspector.domain.schema.models.SchemaType
import im.dino.dbinspector.ui.content.table.TableViewModel
import im.dino.dbinspector.ui.content.trigger.TriggerViewModel
import im.dino.dbinspector.ui.content.view.ViewViewModel

internal class ContentViewModelFactory(
    private val type: SchemaType,
    private val path: String,
    private val name: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (type) {
            SchemaType.TABLE -> TableViewModel(path, name) as T
            SchemaType.TRIGGER -> TriggerViewModel(path, name) as T
            SchemaType.VIEW -> ViewViewModel(path, name) as T
        }
}
