package com.infinum.dbinspector.ui.edit.history

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.history.models.Execution
import com.infinum.dbinspector.domain.history.models.History
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

internal class HistoryViewModel(
    private val getHistory: UseCases.GetHistory,
    private val clearHistory: UseCases.ClearHistory,
    private val removeExecution: UseCases.RemoveExecution
) : BaseViewModel() {

    fun history(
        databasePath: String,
        onData: suspend (value: History) -> Unit
    ) =
        launch {
            getHistory(HistoryParameters.All(databasePath))
                .flowOn(runningDispatchers)
                .catch { throwable -> errorHandler.handleException(coroutineContext, throwable) }
                .collectLatest {
                    onData(it)
                }
        }

    fun clearHistory(
        databasePath: String
    ) =
        launch {
            io {
                clearHistory(HistoryParameters.All(databasePath))
            }
        }

    fun clearExecution(databasePath: String, execution: Execution) =
        launch {
            io {
                removeExecution(
                    HistoryParameters.Execution(
                        databasePath = databasePath,
                        statement = execution.statement,
                        timestamp = execution.timestamp,
                        isSuccess = execution.isSuccessful
                    )
                )
            }
        }
}
