package im.dino.dbinspector.ui.schema.views

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import im.dino.dbinspector.ui.shared.bus.EventBus
import im.dino.dbinspector.ui.shared.bus.models.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

internal class ViewsViewModel : BaseViewModel() {

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun observe(action: suspend () -> Unit) =
        launch {
            io {
                EventBus.on<Event.Refresh>().collectLatest { action() }
            }
        }

    fun query(path: String, args: String? = null, action: suspend (value: PagingData<String>) -> Unit) {
        launch {
            Pager(PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true)) {
                ViewsDataSource(path, PAGE_SIZE, args)
            }
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { action(it) }
        }
    }
}