package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class GetSettingsUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.GetSettings {

    override suspend fun invoke(input: SettingsParameters.Get): Settings =
        settingsRepository.getPage(input)
}
