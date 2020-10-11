package im.dino.dbinspector.ui.pragma.shared

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.ui.shared.base.BaseDataSource
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

internal abstract class PragmaSourceViewModel : BaseViewModel() {

    abstract fun pragmaStatement(name: String): String

    abstract fun dataSource(databasePath: String, statement: String): BaseDataSource

    lateinit var databasePath: String

    fun query(
        schemaName: String,
        onData: suspend (value: PagingData<String>) -> Unit
    ) {
        launch {
            pageFlow(databasePath, pragmaStatement(schemaName)) {
                onData(it)
            }
        }
    }

    private suspend fun pageFlow(
        databasePath: String,
        statement: String,
        onData: suspend (value: PagingData<String>) -> Unit
    ) =
        Pager(pagingConfig) {
            dataSource(databasePath, statement)
        }
            .flow
            .cachedIn(viewModelScope)
            .collectLatest { onData(it) }
}
