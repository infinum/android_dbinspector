package im.dino.dbinspector.ui.schema.tables

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.ui.schema.shared.SchemaViewModel
import kotlinx.coroutines.flow.collectLatest

internal class TablesViewModel : SchemaViewModel() {

    override fun observe(action: suspend () -> Unit) = Unit

    override fun query(
        path: String,
        argument: String?,
        onData: suspend (value: PagingData<String>) -> Unit,
        onEmpty: suspend (value: Boolean) -> Unit
    ) {
        launch {
            Pager(PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false)) {
                TablesDataSource(path, PAGE_SIZE, argument, onEmpty)
            }
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { onData(it) }
        }
    }
}