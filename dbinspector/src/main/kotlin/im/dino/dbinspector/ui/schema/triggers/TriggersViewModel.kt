package im.dino.dbinspector.ui.schema.triggers

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.ui.schema.shared.SchemaViewModel
import im.dino.dbinspector.ui.shared.bus.EventBus
import im.dino.dbinspector.ui.shared.bus.models.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

internal class TriggersViewModel : SchemaViewModel() {

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun observe(action: suspend () -> Unit) {
        launch {
            io {
                EventBus.on<Event.RefreshTriggers>().collectLatest { action() }
            }
        }
    }

    override fun query(
        path: String,
        argument: String?,
        onData: suspend (value: PagingData<String>) -> Unit,
        onEmpty: suspend (value: Boolean) -> Unit
    ) {
        launch {
            Pager(PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true)) {
                TriggersDataSource(path, PAGE_SIZE, argument, onEmpty)
            }
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { onData(it) }
        }
    }
}