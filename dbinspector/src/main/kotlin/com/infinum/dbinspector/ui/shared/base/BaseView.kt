package com.infinum.dbinspector.ui.shared.base

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.collectLatest

internal interface BaseView<State, Event> {

    val viewModel: BaseViewModel<State, Event>?

    fun onState(state: State)

    fun onEvent(event: Event)

    fun onError(error: Throwable) {}

    fun collectFlows(lifecycleCoroutineScope: LifecycleCoroutineScope) {
        lifecycleCoroutineScope.launchWhenStarted {
            viewModel?.stateFlow?.collectLatest { state ->
                state?.let { onState(it) }
            }
        }
        lifecycleCoroutineScope.launchWhenStarted {
            viewModel?.eventFlow?.collectLatest {
                onEvent(it)
            }
        }
        lifecycleCoroutineScope.launchWhenStarted {
            viewModel?.errorFlow?.collectLatest { throwable ->
                throwable?.let { onError(it) }
            }
        }
    }
}
