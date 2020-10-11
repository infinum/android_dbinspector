package im.dino.dbinspector.ui.schema.shared

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

internal abstract class SchemaSourceViewModel : BaseViewModel() {

    abstract fun dataSource(databasePath: String): SchemaDataSource

    abstract fun observe(action: suspend () -> Unit)

    abstract fun query(
        databasePath: String,
        onData: suspend (value: PagingData<String>) -> Unit
    )

    internal suspend fun pageFlow(
        databasePath: String,
        onData: suspend (value: PagingData<String>) -> Unit
    ) =
        Pager(pagingConfig) {
            dataSource(databasePath)
        }
            .flow
            .cachedIn(viewModelScope)
            .collectLatest { onData(it) }
}
