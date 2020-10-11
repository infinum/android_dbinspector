package im.dino.dbinspector.domain.pragma.usecases

import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query

internal class GetTriggerInfoUseCase(
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetTriggerInfo {

    override suspend fun invoke(input: Query): Page {
        return pragmaRepository.getTriggerInfo(input)
    }
}