package com.infinum.dbinspector.domain.settings.mappers

import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.ui.shared.Constants

internal class SettingsMapper(
    private val truncateModeMapper: Mappers.TruncateMode,
    private val blobPreviewModeMapper: Mappers.BlobPreviewMode
) : Mappers.Settings {

    override fun truncateModeMapper() = truncateModeMapper

    override fun blobPreviewModeMapper() = blobPreviewModeMapper

    override fun mapLocalToDomain(model: SettingsEntity): Settings =
        Settings(
            linesLimitEnabled = model.linesLimit,
            linesCount = if (model.linesCount == 0) {
                Constants.Settings.LINES_LIMIT_MAXIMUM
            } else {
                model.linesCount
            },
            truncateMode = truncateModeMapper.mapLocalToDomain(model.truncateMode),
            blobPreviewMode = blobPreviewModeMapper.mapLocalToDomain(model.blobPreview)
        )
}
