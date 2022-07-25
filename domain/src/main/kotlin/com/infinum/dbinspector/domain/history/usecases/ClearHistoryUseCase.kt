package com.infinum.dbinspector.domain.history.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters

internal class ClearHistoryUseCase(
    private val historyRepository: Repositories.History
) : UseCases.ClearHistory {

    override suspend fun invoke(input: HistoryParameters.All) =
        historyRepository.clearByDatabase(input)
}
