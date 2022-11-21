package com.infinum.dbinspector.domain.settings

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

@Suppress("LongParameterList")
internal class SettingsRepository(
    private val getSettings: Interactors.GetSettings,
    private val linesLimit: Interactors.SaveLinesLimit,
    private val linesCount: Interactors.SaveLinesCount,
    private val truncateMode: Interactors.SaveTruncateMode,
    private val blobPreviewMode: Interactors.SaveBlobPreviewMode,
    private val saveIgnoredTableName: Interactors.SaveIgnoredTableName,
    private val removeIgnoredTableName: Interactors.RemoveIgnoredTableName,
    private val saveServerPort: Interactors.SaveServerPort,
    private val startWebServer: Interactors.StartServer,
    private val stopWebServer: Interactors.StopServer,
    private val control: Control.Settings
) : Repositories.Settings {

    override suspend fun getPage(input: SettingsParameters.Get): Settings =
        getSettings(control.converter get input).let { control.mapper(it) }

    override suspend fun saveLinesLimit(input: SettingsParameters.LinesLimit) =
        linesLimit(control.converter linesLimit input)

    override suspend fun saveLinesCount(input: SettingsParameters.LinesCount) =
        linesCount(control.converter linesCount input)

    override suspend fun saveTruncateMode(input: SettingsParameters.Truncate) =
        truncateMode(control.converter truncateMode input)

    override suspend fun saveBlobPreview(input: SettingsParameters.BlobPreview) =
        blobPreviewMode(control.converter blobPreviewMode input)

    override suspend fun saveIgnoredTableName(input: SettingsParameters.IgnoredTableName) =
        saveIgnoredTableName(control.converter ignoredTableName input)

    override suspend fun removeIgnoredTableName(input: SettingsParameters.IgnoredTableName) =
        removeIgnoredTableName(control.converter ignoredTableName input)

    override suspend fun saveServerPort(input: SettingsParameters.ServerPort) =
        saveServerPort(control.converter serverPort input)

    override suspend fun startServer(input: SettingsParameters.ServerPort): Boolean =
       startWebServer(control.converter startServer input)

    override suspend fun stopServer(input: BaseParameters): Boolean =
        stopWebServer(control.converter stopServer input)
}
