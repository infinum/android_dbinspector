package im.dino.dbinspector.ui.pragma.shared

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

internal abstract class PragmaViewModel : BaseViewModel() {

    private val pagingConfig = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true)

    abstract fun source(
        path: String,
        name: String
    ): PagingSource<Int, String>

    internal fun query(
        path: String,
        name: String,
        action: suspend (value: PagingData<String>) -> Unit
    ) {
        launch {
            Pager(
                pagingConfig
            ) {
                source(path, name)
            }
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { action(it) }
        }
    }
}