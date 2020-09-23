package im.dino.dbinspector.ui.tables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.domain.table.models.Table
import kotlinx.coroutines.flow.Flow

class TablesViewModel : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 100
    }

    fun query(path: String, args: String? = null): Flow<PagingData<Table>> {
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE)
        ) {
            TablesDataSource(path, PAGE_SIZE, args)
        }.flow.cachedIn(viewModelScope)
    }
}