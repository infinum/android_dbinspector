package im.dino.dbinspector.ui.schema.shared

import androidx.paging.PagingData
import im.dino.dbinspector.ui.shared.base.BaseViewModel

internal abstract class SchemaViewModel : BaseViewModel() {

    abstract fun observe(action: suspend () -> Unit)

    abstract fun query(
        path: String,
        argument: String? = null,
        onData: suspend (value: PagingData<String>) -> Unit,
        onEmpty: suspend (value: Boolean) -> Unit
    )
}