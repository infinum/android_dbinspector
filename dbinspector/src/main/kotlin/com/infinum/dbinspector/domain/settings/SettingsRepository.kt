package com.infinum.dbinspector.domain.settings

import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class SettingsRepository(
    private val getSettings: Interactors.GetSettings,
    private val linesLimit: Interactors.SaveLinesLimit,
    private val linesCount: Interactors.SaveLinesCount,
    private val truncateMode: Interactors.SaveTruncateMode,
    private val blobPreviewMode: Interactors.SaveBlobPreviewMode,
    private val mapper: Mappers.Settings
) : Repositories.Settings {

    override suspend fun getPage(input: SettingsParameters.Get): Settings =
        getSettings(Unit)?.let { mapper.mapLocalToDomain(it) } ?: Settings()

    override suspend fun saveLinesLimit(input: SettingsParameters.LinesLimit) =
        linesLimit(input.isEnabled)

    override suspend fun saveLinesCount(input: SettingsParameters.LinesCount) =
        linesCount(input.count)

    override suspend fun saveTruncateMode(input: SettingsParameters.Truncate) =
        truncateMode(mapper.truncateModeMapper().mapDomainToLocal(input.mode))

    override suspend fun saveBlobPreview(input: SettingsParameters.BlobPreview) =
        blobPreviewMode(mapper.blobPreviewModeMapper().mapDomainToLocal(input.mode))
}
