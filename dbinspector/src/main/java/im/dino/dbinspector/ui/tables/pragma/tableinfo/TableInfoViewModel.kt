package im.dino.dbinspector.ui.tables.pragma.tableinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class TableInfoViewModel : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 100
    }

    fun query(path: String, name: String) =
        Pager(
            PagingConfig(pageSize = PAGE_SIZE)
        ) {
            TableInfoDataSource(path, name, PAGE_SIZE)
        }
            .flow.cachedIn(viewModelScope)
}