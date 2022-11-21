package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class StopServerUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.StopServer {

    override suspend fun invoke(input: BaseParameters): Boolean =
        settingsRepository.stopServer(input)
}
