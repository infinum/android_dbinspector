package com.infinum.dbinspector.ui.shared.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinum.dbinspector.data.sources.memory.logger.Logger
import com.infinum.dbinspector.di.LibraryKoin
import com.infinum.dbinspector.di.LibraryKoinComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal abstract class BaseViewModel : ViewModel(), LibraryKoinComponent {

    private val logger: Logger? = LibraryKoin.koin().getOrNull()

    private val supervisorJob = SupervisorJob()

    protected val runningScope = viewModelScope
    protected val runningDispatchers = Dispatchers.IO
    private var mainDispatchers = Dispatchers.Main

    protected open val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.message?.let { logger?.error(it) }
    }

    override fun onCleared() {
        super.onCleared()
        runningDispatchers.cancel()
        runningScope.cancel()
        supervisorJob.cancel()
    }

    protected fun launch(scope: CoroutineScope = runningScope, block: suspend CoroutineScope.() -> Unit) {
        scope.launch(errorHandler + mainDispatchers + supervisorJob) { block.invoke(this) }
    }

    protected suspend fun <T> io(block: suspend CoroutineScope.() -> T) =
        withContext(context = runningDispatchers) { block.invoke(this) }
}
