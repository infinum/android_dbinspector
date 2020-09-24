package im.dino.dbinspector.ui.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {

    companion object {
        internal const val PAGE_SIZE = 100
    }

    private val dispatchersIo = Dispatchers.IO

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    override fun onCleared() {
        dispatchersIo.cancel()
    }

    fun launch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(errorHandler + Dispatchers.Main) { block.invoke(this) }

    suspend fun <T> io(block: suspend CoroutineScope.() -> T) =
        withContext(context = dispatchersIo) { block.invoke(this) }
}