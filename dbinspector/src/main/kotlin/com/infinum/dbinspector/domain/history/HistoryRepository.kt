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
    private val control: Control.History
) : Repositories.History {

    override fun getByDatabase(input: HistoryParameters.Get): Flow<History> =
        getHistory(control.converter get input).map { control.mapper(it) }

    override suspend fun saveExecution(input: HistoryParameters.Save) =
        saveHistoryExecution(control.converter save input)
}
