package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.settings.models.Settings

internal class LoadAllSettingsUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.LoadAllSettings {

    override suspend fun invoke(input: Unit): Settings =
        settingsRepository.load()
}
