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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal abstract class BaseViewModel<State, Event> : ViewModel(), LibraryKoinComponent {

    private val logger: Logger? = LibraryKoin.koin().getOrNull()

    private val supervisorJob = SupervisorJob()

    protected val runningScope = viewModelScope
    protected val runningDispatchers = Dispatchers.IO
    private var mainDispatchers = Dispatchers.Main

    private val mutableStateFlow: MutableStateFlow<State?> = MutableStateFlow(null)
    private val mutableEventFlow: MutableSharedFlow<Event> = MutableSharedFlow(replay = 1)
    private val mutableErrorFlow: MutableStateFlow<Throwable?> = MutableStateFlow(null)

    val stateFlow: StateFlow<State?> get() = mutableStateFlow.asStateFlow()
    val eventFlow: SharedFlow<Event> get() = mutableEventFlow.asSharedFlow()
    val errorFlow: StateFlow<Throwable?> get() = mutableErrorFlow.asStateFlow()

    protected open val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.message?.let { logger?.error(it) }
        mutableErrorFlow.value = throwable
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

    protected fun setState(state: State) {
        mutableStateFlow.value = state
    }

    protected suspend fun emitEvent(event: Event) {
        mutableEventFlow.emit(event)
    }

    protected fun setError(error: Throwable) {
        mutableErrorFlow.value = error
    }
}
