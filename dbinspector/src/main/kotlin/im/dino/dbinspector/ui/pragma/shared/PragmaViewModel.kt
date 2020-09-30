package im.dino.dbinspector.ui.pragma.shared

import androidx.paging.PagingData
import im.dino.dbinspector.ui.shared.base.BaseViewModel

internal abstract class PragmaViewModel : BaseViewModel() {

    abstract fun query(
        path: String,
        name: String,
        action: suspend (value: PagingData<String>) -> Unit
    )
}