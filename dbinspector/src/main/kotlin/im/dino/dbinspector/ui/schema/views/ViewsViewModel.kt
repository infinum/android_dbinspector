package im.dino.dbinspector.ui.schema.views

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.ui.shared.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

internal class ViewsViewModel : BaseViewModel() {

    fun query(path: String, args: String? = null, action: suspend (value: PagingData<String>) -> Unit) {
        launch {
            Pager(PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true)) {
                ViewsDataSource(path, PAGE_SIZE, args)
            }
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { action(it) }
        }
    }
}