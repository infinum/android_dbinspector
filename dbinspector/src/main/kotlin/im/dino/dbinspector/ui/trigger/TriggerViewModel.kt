package im.dino.dbinspector.ui.trigger

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.domain.schema.models.TriggerColumns
import im.dino.dbinspector.domain.schema.trigger.DropTriggerOperation
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

internal class TriggerViewModel(
    private val path: String,
    name: String
) : BaseViewModel() {

    private val dataSource = TriggerDataSource(path, name, PAGE_SIZE)

    private val dropTriggerOperation = DropTriggerOperation(name, PAGE_SIZE)

    fun header(action: suspend (value: List<String>) -> Unit) =
        launch {
            val result = io { TriggerColumns.values().map { it.name.toLowerCase() } }
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
                dropTriggerOperation(path, null)
            }
            action()
        }
}