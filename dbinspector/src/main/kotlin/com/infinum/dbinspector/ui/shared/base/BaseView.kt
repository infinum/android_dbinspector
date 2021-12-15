package com.infinum.dbinspector.ui.shared.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal interface BaseView<State, Event> {

    val viewModel: BaseViewModel<State, Event>?

    fun onState(state: State)

    fun onEvent(event: Event)

    fun onError(error: Throwable) {}

    fun collectFlows(lifecycleCoroutineScope: LifecycleCoroutineScope, lifecycle: Lifecycle) {
        lifecycleCoroutineScope.launch {
            viewModel?.stateFlow
                ?.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                ?.collectLatest { state -> state?.let { onState(it) } }
        }
        lifecycleCoroutineScope.launch {
            viewModel?.eventFlow
                ?.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                ?.collectLatest { onEvent(it) }
        }
        lifecycleCoroutineScope.launch {
            viewModel?.errorFlow
                ?.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                ?.collectLatest { throwable -> throwable?.let { onError(it) } }
        }
    }
}
