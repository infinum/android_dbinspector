package com.infinum.dbinspector.ui.edit.history

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.history.models.Execution
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.ui.shared.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

internal class HistoryViewModel(
    private val getHistory: com.infinum.dbinspector.domain.UseCases.GetHistory,
    private val clearHistory: com.infinum.dbinspector.domain.UseCases.ClearHistory,
    private val removeExecution: com.infinum.dbinspector.domain.UseCases.RemoveExecution
) : BaseViewModel<HistoryState, Any>() {

    fun history(databasePath: String) =
        launch {
            getHistory(HistoryParameters.All(databasePath))
                .flowOn(runningDispatchers)
                .catch { errorHandler.handleException(coroutineContext, it) }
                .collectLatest {
                    setState(HistoryState.History(history = it))
                }
        }

    fun clearHistory(databasePath: String) =
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
