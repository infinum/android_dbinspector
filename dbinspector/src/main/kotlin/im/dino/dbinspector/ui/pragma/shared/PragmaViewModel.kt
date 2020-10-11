package im.dino.dbinspector.ui.pragma.shared

import androidx.paging.PagingData
import androidx.paging.PagingSource
import im.dino.dbinspector.ui.shared.base.BaseViewModel

internal abstract class PragmaViewModel : BaseViewModel() {

    abstract fun source(
        path: String,
        name: String
    ): PagingSource<Int, String>

    internal fun query(
        path: String,
        name: String,
        onData: suspend (value: PagingData<String>) -> Unit
    ) {
//        launch {
//            flow(
//                source(path, name)
//            ) {
//                onData(it)
//            }
//        }
    }
}
