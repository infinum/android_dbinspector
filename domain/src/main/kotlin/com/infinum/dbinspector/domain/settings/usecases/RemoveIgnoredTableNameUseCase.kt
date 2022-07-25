package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class RemoveIgnoredTableNameUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.RemoveIgnoredTableName {

    override suspend fun invoke(input: SettingsParameters.IgnoredTableName) =
        settingsRepository.removeIgnoredTableName(input)
}
