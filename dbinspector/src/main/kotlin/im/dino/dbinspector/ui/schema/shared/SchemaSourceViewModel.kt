package im.dino.dbinspector.ui.schema.shared

import androidx.paging.PagingData
import im.dino.dbinspector.ui.shared.paging.PagingViewModel

internal abstract class SchemaSourceViewModel : PagingViewModel() {

    abstract fun schemaStatement(query: String?): String

    abstract fun query(
        databasePath: String,
        query: String?,
        onData: suspend (value: PagingData<String>) -> Unit
    )
}
