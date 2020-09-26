package im.dino.dbinspector.ui.pragma.tableinfo

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

internal class TableInfoViewModel : BaseViewModel() {

    fun query(path: String, name: String, action: suspend (value: PagingData<String>) -> Unit) {
        launch {
            Pager(
                PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true)
            ) {
                TableInfoDataSource(path, name, PAGE_SIZE)
            }
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { action(it) }
        }
    }
}