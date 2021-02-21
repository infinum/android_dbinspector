package com.infinum.dbinspector.domain.history.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters

internal class SaveExecutionUseCase(
    private val historyRepository: Repositories.History
) : UseCases.SaveExecution {

    override suspend fun invoke(input: HistoryParameters.Save) =
        historyRepository.saveExecution(input)
}
