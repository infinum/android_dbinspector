package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class AutoStartServerUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.AutoStartServer {

    override suspend fun invoke(input: BaseParameters): Unit =
        settingsRepository.getPage(SettingsParameters.Get()).let { settings: Settings ->
            if (settings.serverRunning) {
                settingsRepository.startServer(
                    SettingsParameters.StartServer(
                        settings.serverPort,
                        true
                    )
                )
            }
        }
}
