package com.infinum.dbinspector.domain.history.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.history.models.History
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import kotlinx.coroutines.flow.Flow

internal class GetHistoryUseCase(
    private val historyRepository: Repositories.History
) : UseCases.GetHistory {

    override fun invoke(input: HistoryParameters.All): Flow<History> =
        historyRepository.getByDatabase(input)
}
