package im.dino.dbinspector.ui.pragma.shared

import androidx.paging.PagingData
import im.dino.dbinspector.ui.shared.base.PagingViewModel

internal abstract class PragmaSourceViewModel : PagingViewModel() {

    abstract fun pragmaStatement(name: String): String

    lateinit var databasePath: String

    fun query(
        schemaName: String,
        onData: suspend (value: PagingData<String>) -> Unit
    ) {
        launch {
            pageFlow(databasePath, pragmaStatement(schemaName)) {
                onData(it)
            }
        }
    }
}
