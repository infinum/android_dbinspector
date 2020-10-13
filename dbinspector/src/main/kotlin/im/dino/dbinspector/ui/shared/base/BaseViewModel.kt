package im.dino.dbinspector.ui.shared.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import im.dino.dbinspector.ui.shared.Constants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal abstract class BaseViewModel : ViewModel() {

    internal val pagingConfig = PagingConfig(
        pageSize = Constants.Limits.PAGE_SIZE,
        enablePlaceholders = true
    )

    private val supervisorJob = SupervisorJob()

    private val dispatchersIo = Dispatchers.IO

    val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    override fun onCleared() {
        super.onCleared()
        dispatchersIo.cancel()
        supervisorJob.cancel()
    }

    internal fun launch(scope: CoroutineScope = viewModelScope, block: suspend CoroutineScope.() -> Unit) {
        scope.launch(errorHandler + Dispatchers.Main + supervisorJob) { block.invoke(this) }
    }

    internal suspend fun <T> io(block: suspend CoroutineScope.() -> T) =
        withContext(context = dispatchersIo) { block.invoke(this) }
}
