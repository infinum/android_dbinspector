package im.dino.dbinspector.ui.shared.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal abstract class BaseViewModel : ViewModel() {

    companion object {
        internal const val PAGE_SIZE = 100
    }

    private val pagingConfig = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true)

    private val dispatchersIo = Dispatchers.IO

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    override fun onCleared() {
        dispatchersIo.cancel()
    }

    internal fun launch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(errorHandler + Dispatchers.Main) { block.invoke(this) }

    internal suspend fun <T> io(block: suspend CoroutineScope.() -> T) =
        withContext(context = dispatchersIo) { block.invoke(this) }

    internal suspend fun flow(pagingSource: PagingSource<Int, String>, onData: suspend (value: PagingData<String>) -> Unit) =
        Pager(pagingConfig) {
            pagingSource
        }
            .flow
            .cachedIn(viewModelScope)
            .collectLatest { onData(it) }
}