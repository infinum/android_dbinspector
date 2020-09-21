package im.dino.dbinspector.ui.tables.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContentViewModelFactory(
    private val path: String,
    private val name: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContentViewModel(path, name) as T
    }
}