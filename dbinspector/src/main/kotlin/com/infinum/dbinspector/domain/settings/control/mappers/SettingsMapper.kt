package com.infinum.dbinspector.domain.settings.control.mappers

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.settings.models.Settings

internal class SettingsMapper(
    private val truncateModeMapper: Mappers.TruncateMode,
    private val blobPreviewModeMapper: Mappers.BlobPreviewMode
) : Mappers.Settings {

    override suspend fun invoke(model: SettingsEntity): Settings =
        Settings(
            linesLimitEnabled = model.linesLimit,
            linesCount = if (model.linesCount == 0) {
                Domain.Constants.Settings.LINES_LIMIT_MAXIMUM
            } else {
                model.linesCount
            },
            truncateMode = truncateModeMapper(model.truncateMode),
            blobPreviewMode = blobPreviewModeMapper(model.blobPreview),
            ignoredTableNames = model.ignoredTableNamesList.map { it.name }
        )
}
