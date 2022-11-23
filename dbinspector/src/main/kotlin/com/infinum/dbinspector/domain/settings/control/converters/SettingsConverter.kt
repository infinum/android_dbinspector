package com.infinum.dbinspector.domain.settings.control.converters

import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class SettingsConverter(
    private val truncateModeConverter: Converters.TruncateMode,
    private val blobPreviewConverter: Converters.BlobPreview
) : Converters.Settings {

    override suspend fun get(parameters: SettingsParameters.Get): SettingsTask =
        SettingsTask()

    override suspend fun linesLimit(parameters: SettingsParameters.LinesLimit): SettingsTask =
        SettingsTask(linesLimited = parameters.isEnabled)

    override suspend fun linesCount(parameters: SettingsParameters.LinesCount): SettingsTask =
        SettingsTask(
            linesCount = if (parameters.count == 0) {
                Domain.Constants.Settings.LINES_LIMIT_MAXIMUM
            } else {
                parameters.count
            }
        )

    override suspend fun truncateMode(parameters: SettingsParameters.Truncate): SettingsTask =
        SettingsTask(truncateMode = truncateModeConverter(parameters))

    override suspend fun blobPreviewMode(parameters: SettingsParameters.BlobPreview): SettingsTask =
        SettingsTask(blobPreviewMode = blobPreviewConverter(parameters))

    override suspend fun ignoredTableName(parameters: SettingsParameters.IgnoredTableName): SettingsTask =
        SettingsTask(ignoredTableName = parameters.name)

    override suspend fun serverPort(parameters: SettingsParameters.ServerPort): SettingsTask =
        SettingsTask(serverPort = parameters.port)

    override suspend fun startServer(parameters: SettingsParameters.StartServer): SettingsTask =
        SettingsTask(serverPort = parameters.port, serverState = parameters.state)

    override suspend fun stopServer(parameters: SettingsParameters.StopServer): SettingsTask =
        SettingsTask(serverState = parameters.state)
}
