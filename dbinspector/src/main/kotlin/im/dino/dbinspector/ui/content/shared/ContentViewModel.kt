package im.dino.dbinspector.ui.content.shared

import androidx.paging.PagingData
import androidx.paging.PagingSource
import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractDatabaseOperation
import im.dino.dbinspector.ui.shared.base.BaseViewModel

internal abstract class ContentViewModel(
    private val path: String
) : BaseViewModel() {

    abstract val nameOrdinal: Int

    abstract fun source(): PagingSource<Int, String>

    abstract fun info(): AbstractDatabaseOperation<List<Row>>

    abstract fun drop(): AbstractDatabaseOperation<List<Row>>

    fun header(action: suspend (value: List<String>) -> Unit) =
        launch {
            val result = io {
                info()(path, null).map { it.fields[nameOrdinal] }
            }
            action(result)
        }

    fun query(onData: suspend (value: PagingData<String>) -> Unit) {
        launch {
            flow(
                source()
            ) {
                onData(it)
            }
        }
    }

    fun drop(action: suspend (value: PagingData<String>) -> Unit) =
        launch {
            val result = io {
                drop()(path, null)
            }
            if (result.isEmpty()) {
                query(action)
            }
        }
}
