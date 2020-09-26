package im.dino.dbinspector.ui.table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

internal class TableViewModelFactory(
    private val path: String,
    private val name: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TableViewModel(path, name) as T
    }
}