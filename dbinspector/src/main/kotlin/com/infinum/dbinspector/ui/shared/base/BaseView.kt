package com.infinum.dbinspector.ui.shared.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal interface BaseView<State, Event> {

    val viewModel: BaseViewModel<State, Event>?

    fun onState(state: State)

    fun onEvent(event: Event)

    fun onError(error: Throwable) {}

    fun collectFlows(owner: LifecycleOwner) {
        with(owner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel?.stateFlow?.collectLatest { state -> state?.let { onState(it) } }
                }
            }
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel?.eventFlow?.collectLatest { event -> event?.let { onEvent(it) } }
                }
            }
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel?.errorFlow?.collectLatest { throwable -> throwable?.let { onError(it) } }
                }
            }
        }
    }
}
