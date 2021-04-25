package com.infinum.dbinspector.ui.shared.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinum.dbinspector.data.source.memory.logger.Logger
import com.infinum.dbinspector.di.LibraryKoinComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.inject

internal abstract class BaseViewModel : ViewModel(), LibraryKoinComponent {

    private val logger: Logger by inject()

    private val supervisorJob = SupervisorJob()

    private val dispatchersIo = Dispatchers.IO

    protected open val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.message?.let { logger.error(it) }
    }

    override fun onCleared() {
        super.onCleared()
        dispatchersIo.cancel()
        supervisorJob.cancel()
    }

    protected fun launch(scope: CoroutineScope = viewModelScope, block: suspend CoroutineScope.() -> Unit) {
        scope.launch(errorHandler + Dispatchers.Main + supervisorJob) { block.invoke(this) }
    }

    protected suspend fun <T> io(block: suspend CoroutineScope.() -> T) =
        withContext(context = dispatchersIo) { block.invoke(this) }
}
