package im.dino.dbinspector.ui.table

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.domain.pragma.TableInfoOperation
import im.dino.dbinspector.domain.pragma.models.TableInfoColumns
import im.dino.dbinspector.domain.schema.table.ClearTableOperation
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

internal class TableViewModel(
    private val path: String,
    name: String
) : BaseViewModel() {

    private var job: Job? = null

    private val infoDataSource = TableInfoOperation(name, PAGE_SIZE)(path, null)

    private val dataSource = TableDataSource(path, name, PAGE_SIZE)

    private val clearTableOperation = ClearTableOperation(name)

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        job = null
    }

    fun header(action: suspend (value: List<String>) -> Unit) =
        launch {
            val result = io { infoDataSource.map { it.fields[TableInfoColumns.NAME.ordinal] } }
            action(result)
        }


    fun query(action: suspend (value: PagingData<String>) -> Unit) {
        launch {
            Pager(
                PagingConfig(
                    pageSize = PAGE_SIZE,
                    prefetchDistance = PAGE_SIZE * 3,
                    enablePlaceholders = true
                )
            ) {
                dataSource
            }
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { action(it) }
        }
    }

    fun clear(action: suspend (value: PagingData<String>) -> Unit) =
        launch {
            val ok = io {
                clearTableOperation(path, null)
            }
            if (ok) {
                query(action)
            }
        }
}