package com.infinum.dbinspector.domain.history

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.history.models.History
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HistoryRepository(
    private val getHistory: Interactors.GetHistory,
    private val saveHistoryExecution: Interactors.SaveExecution,
    private val clearHistory: Interactors.ClearHistory,
    private val removeHistoryExecution: Interactors.RemoveExecution,
    private val control: Control.History
) : Repositories.History {

    override fun getByDatabase(input: HistoryParameters.All): Flow<History> =
        getHistory(control.converter get input).map { control.mapper(it) }

    override suspend fun saveExecution(input: HistoryParameters.Execution) =
        saveHistoryExecution(control.converter execution input)

    override suspend fun clearByDatabase(input: HistoryParameters.All) =
        clearHistory(control.converter clear input)

    override suspend fun removeExecution(input: HistoryParameters.Execution) =
        removeHistoryExecution(control.converter execution input)
}
