package com.infinum.dbinspector.domain.pragma.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Query

internal class GetTriggerInfoUseCase(
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetTriggerInfo {

    override suspend fun invoke(input: Query): Page {
        return pragmaRepository.getTriggerInfo(input)
    }
}
