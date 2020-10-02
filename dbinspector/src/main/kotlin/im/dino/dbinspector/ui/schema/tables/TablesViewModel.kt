package im.dino.dbinspector.ui.schema.tables

import androidx.paging.PagingData
import androidx.paging.PagingSource
import im.dino.dbinspector.ui.schema.shared.SchemaViewModel

internal class TablesViewModel : SchemaViewModel() {

    override fun source(
        path: String,
        argument: String?,
        onEmpty: suspend (value: Boolean) -> Unit
    ) = TablesDataSource(path, PAGE_SIZE, argument, onEmpty)

    override fun observe(action: suspend () -> Unit) = Unit

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