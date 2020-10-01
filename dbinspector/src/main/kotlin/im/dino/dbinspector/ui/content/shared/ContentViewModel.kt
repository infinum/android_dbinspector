package im.dino.dbinspector.ui.content.shared

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractDatabaseOperation
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

internal abstract class ContentViewModel(
    private val path: String
) : BaseViewModel() {

    abstract val nameOrdinal: Int

    abstract fun source(): PagingSource<Int, String>

    abstract fun info(): AbstractDatabaseOperation<List<Row>>

    abstract fun drop(): AbstractDatabaseOperation<List<Row>>

    fun header(action: suspend (value: List<String>) -> Unit) =
        launch {
            val result = io {
                info()(path, null).map { it.fields[nameOrdinal] }
            }
            action(result)
        }

    fun query(action: suspend (value: PagingData<String>) -> Unit) {
        launch {
            Pager(
                config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = PAGE_SIZE * 3),
                pagingSourceFactory = this@ContentViewModel::source
            )
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { action(it) }
        }
    }

    fun drop(action: suspend (value: PagingData<String>) -> Unit) =
        launch {
            val result = io {
                drop()(path, null)
            }
            if (result.isEmpty()) {
                query(action)
            }
        }
}
