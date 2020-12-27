package com.infinum.dbinspector.domain.settings.usecases

import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases

internal class SaveBlobPreviewModeUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.SaveBlobPreviewMode {

    override suspend fun invoke(input: BlobPreviewType) =
        settingsRepository.saveBlobPreview(input)
}
