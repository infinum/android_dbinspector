package im.dino.dbinspector.ui.trigger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

internal class TriggerViewModelFactory(
    private val path: String,
    private val name: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TriggerViewModel(path, name) as T
    }
}