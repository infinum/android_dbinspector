package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class SaveServerPortUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.SaveServerPort {

    override suspend fun invoke(input: SettingsParameters.ServerPort) =
        settingsRepository.saveServerPort(input)
}
