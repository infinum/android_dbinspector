package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class StartServerUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.StartServer {

    override suspend fun invoke(input: SettingsParameters.StartServer): Settings =
        settingsRepository.startServer(input)
}
