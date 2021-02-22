package com.infinum.dbinspector.domain.history.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters

internal class RemoveExecutionUseCase(
    private val historyRepository: Repositories.History
) : UseCases.RemoveExecution {

    override suspend fun invoke(input: HistoryParameters.Execution) =
        historyRepository.removeExecution(input)
}
