package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class SaveLinesCountUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.SaveLinesCount {

    override suspend fun invoke(input: SettingsParameters.LinesCount) =
        settingsRepository.saveLinesCount(input)
}
