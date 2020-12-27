package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.settings.models.Settings

internal class ToggleLinesLimitUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.ToggleLinesLimit {

    override suspend fun invoke(input: Boolean) =
        settingsRepository.saveLinesLimit(input)
}
