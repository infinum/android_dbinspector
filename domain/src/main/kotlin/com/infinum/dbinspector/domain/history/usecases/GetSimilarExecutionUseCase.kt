package com.infinum.dbinspector.domain.history.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.history.models.History
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters

internal class GetSimilarExecutionUseCase(
    private val historyRepository: Repositories.History
) : UseCases.GetSimilarExecution {

    override suspend fun invoke(input: HistoryParameters.Execution): History =
        historyRepository.getSimilarExecution(input)
}
