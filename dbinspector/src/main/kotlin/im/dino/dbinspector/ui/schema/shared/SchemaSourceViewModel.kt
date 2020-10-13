package im.dino.dbinspector.ui.schema.shared

import androidx.paging.PagingData
import im.dino.dbinspector.ui.shared.base.PagingViewModel

internal abstract class SchemaSourceViewModel : PagingViewModel() {

    abstract fun schemaStatement(): String

    abstract fun observe(action: suspend () -> Unit)

    abstract fun query(
        databasePath: String,
        onData: suspend (value: PagingData<String>) -> Unit
    )
}
