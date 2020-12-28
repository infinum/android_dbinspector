package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class SaveTruncateModeUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.SaveTruncateMode {

    override suspend fun invoke(input: SettingsParameters.TruncateMode) =
        settingsRepository.saveTruncateMode(input)
}
