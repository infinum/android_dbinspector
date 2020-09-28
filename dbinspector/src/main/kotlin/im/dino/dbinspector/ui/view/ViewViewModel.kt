package im.dino.dbinspector.ui.view

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.domain.pragma.schema.TableInfoOperation
import im.dino.dbinspector.domain.pragma.schema.models.TableInfoColumns
import im.dino.dbinspector.domain.schema.view.DropViewOperation
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

internal class ViewViewModel(
    private val path: String,
    name: String
) : BaseViewModel() {

    private var job: Job? = null

    private val infoDataSource = TableInfoOperation(name, PAGE_SIZE)(path, null)

    private val dataSource = ViewDataSource(path, name, PAGE_SIZE)

    private val dropViewOperation = DropViewOperation(name, PAGE_SIZE)

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

    fun drop(action: suspend () -> Unit) =
        launch {
            io {
                dropViewOperation(path, null)
            }
            action()
        }
}