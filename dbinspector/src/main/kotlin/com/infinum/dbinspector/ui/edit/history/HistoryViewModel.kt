package com.infinum.dbinspector.ui.edit.history

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.history.models.History
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

internal class HistoryViewModel(
    private val getHistory: UseCases.GetHistory
) : BaseViewModel() {

    fun history(
        databasePath: String,
        onData: suspend (value: History) -> Unit
    ) =
        launch {
            getHistory(HistoryParameters.Get(databasePath))
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    onData(it)
                }
        }
}