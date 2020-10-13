package im.dino.dbinspector.ui.schema.triggers

import androidx.paging.PagingData
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.schema.shared.SchemaSourceViewModel
import im.dino.dbinspector.ui.shared.bus.EventBus
import im.dino.dbinspector.ui.shared.bus.models.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

internal class TriggersViewModel(
    private val getSchema: UseCases.GetTriggers
) : SchemaSourceViewModel() {

    override fun schemaStatement(): String =
        Statements.Schema.triggers()

    override fun dataSource(databasePath: String, statement: String) = TriggersDataSource(databasePath, statement, getSchema)

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun observe(action: suspend () -> Unit) {
        launch {
            io {
                EventBus.receive<Event.RefreshTriggers>().collectLatest { action() }
            }
        }
    }

    override fun query(
        databasePath: String,
        onData: suspend (value: PagingData<String>) -> Unit
    ) {
        launch {
            pageFlow(databasePath, schemaStatement()) {
                onData(it)
            }
        }
    }
}
