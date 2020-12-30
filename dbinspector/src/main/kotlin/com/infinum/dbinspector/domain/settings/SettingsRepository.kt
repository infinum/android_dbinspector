package com.infinum.dbinspector.domain.settings

import com.infinum.dbinspector.domain.Converters
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
    private val mapper: Mappers.Settings,
    private val converter: Converters.Settings
) : Repositories.Settings {

    override suspend fun getPage(input: SettingsParameters.Get): Settings =
        getSettings(converter get input)?.let { mapper(it) } ?: Settings()

    override suspend fun saveLinesLimit(input: SettingsParameters.LinesLimit) =
        linesLimit(converter linesLimit input)

    override suspend fun saveLinesCount(input: SettingsParameters.LinesCount) =
        linesCount(converter linesCount input)

    override suspend fun saveTruncateMode(input: SettingsParameters.Truncate) =
        truncateMode(converter truncateMode input)

    override suspend fun saveBlobPreview(input: SettingsParameters.BlobPreview) =
        blobPreviewMode(converter blobPreviewMode input)
}
