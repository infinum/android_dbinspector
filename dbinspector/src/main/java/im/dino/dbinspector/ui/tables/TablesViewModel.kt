package im.dino.dbinspector.ui.tables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class TablesViewModel : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 100
    }

    fun query(path: String) =
        Pager(
            PagingConfig(pageSize = PAGE_SIZE)
        ) {
            TablesDataSource(path, PAGE_SIZE)
        }.flow.cachedIn(viewModelScope)
}