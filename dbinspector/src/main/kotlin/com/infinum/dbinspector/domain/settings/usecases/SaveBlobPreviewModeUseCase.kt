package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class SaveBlobPreviewModeUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.SaveBlobPreviewMode {

    override suspend fun invoke(input: SettingsParameters.BlobPreviewMode) =
        settingsRepository.saveBlobPreview(input)
}
