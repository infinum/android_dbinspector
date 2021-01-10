package com.infinum.dbinspector.domain.settings.control.converters

import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class SettingsConverter(
    private val truncateConverter: Converters.Truncate,
    private val blobPreviewConverter: Converters.BlobPreview
) : Converters.Settings {

    override suspend fun get(parameters: SettingsParameters.Get): SettingsTask =
        SettingsTask()

    override suspend fun linesLimit(parameters: SettingsParameters.LinesLimit): SettingsTask =
        SettingsTask(linesLimited = parameters.isEnabled)

    override suspend fun linesCount(parameters: SettingsParameters.LinesCount): SettingsTask =
        SettingsTask(linesCount = parameters.count)

    override suspend fun truncateMode(parameters: SettingsParameters.Truncate): SettingsTask =
        SettingsTask(truncateMode = truncateConverter(parameters))

    override suspend fun blobPreviewMode(parameters: SettingsParameters.BlobPreview): SettingsTask =
        SettingsTask(blobPreviewMode = blobPreviewConverter(parameters))

    override suspend fun ignoredTableName(parameters: SettingsParameters.IgnoredTableName): SettingsTask =
        SettingsTask(ignoredTableName = parameters.name)
}
