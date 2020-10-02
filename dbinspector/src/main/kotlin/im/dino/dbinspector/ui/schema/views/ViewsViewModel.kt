package im.dino.dbinspector.ui.schema.views

import androidx.paging.PagingData
import im.dino.dbinspector.ui.schema.shared.SchemaViewModel
import im.dino.dbinspector.ui.shared.bus.EventBus
import im.dino.dbinspector.ui.shared.bus.models.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

internal class ViewsViewModel : SchemaViewModel() {

    override fun source(
        path: String,
        argument: String?,
        onEmpty: suspend (value: Boolean) -> Unit
    ) =
        ViewsDataSource(path, PAGE_SIZE, argument, onEmpty)

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun observe(action: suspend () -> Unit) {
        launch {
            io {
                EventBus.receive<Event.RefreshViews>().collectLatest { action() }
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
            flow(
                source(path, argument, onEmpty)
            ) {
                onData(it)
            }
        }
    }
}
