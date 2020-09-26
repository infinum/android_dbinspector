package im.dino.dbinspector.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

internal class ViewViewModelFactory(
    private val path: String,
    private val name: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewViewModel(path, name) as T
    }
}