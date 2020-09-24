package im.dino.dbinspector.ui.tables.pragma.indexes

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.ui.shared.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

class IndexViewModel : BaseViewModel() {

    fun query(path: String, name: String, action: suspend (value: PagingData<String>) -> Unit) {
        launch {
            Pager(
                PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true)
            ) {
                IndexDataSource(path, name, PAGE_SIZE)
            }
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { action(it) }
        }
    }
}