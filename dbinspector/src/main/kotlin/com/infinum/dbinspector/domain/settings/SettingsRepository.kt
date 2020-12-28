package com.infinum.dbinspector.domain.settings

import android.text.TextUtils
import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.data.models.local.cursor.TruncateType
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters
import com.infinum.dbinspector.ui.shared.Constants

internal class SettingsRepository(
    private val getSettings: Interactors.GetSettings,
    private val linesLimit: Interactors.SaveLinesLimit,
    private val linesCount: Interactors.SaveLinesCount,
    private val truncateMode: Interactors.SaveTruncateMode,
    private val blobPreviewMode: Interactors.SaveBlobPreviewMode,
) : Repositories.Settings {

    override suspend fun getPage(input: SettingsParameters.Get): Settings =
        getSettings(Unit)?.let {
            Settings(
                linesLimitEnabled = it.linesLimit,
                linesCount = if (it.linesCount == 0) {
                    Constants.Settings.LINES_LIMIT_MAXIMUM
                } else {
                    it.linesCount
                },
                truncateMode = when (it.truncateModeValue) {
                    SettingsEntity.TruncateMode.START_VALUE -> TextUtils.TruncateAt.START
                    SettingsEntity.TruncateMode.MIDDLE_VALUE -> TextUtils.TruncateAt.MIDDLE
                    SettingsEntity.TruncateMode.END_VALUE -> TextUtils.TruncateAt.END
                    else -> TextUtils.TruncateAt.END
                },
                blobPreviewMode = when (it.blobPreview) {
                    SettingsEntity.BlobPreviewMode.PLACEHOLDER -> BlobPreviewMode.PLACEHOLDER
                    SettingsEntity.BlobPreviewMode.UTF8 -> BlobPreviewMode.UTF_8
                    SettingsEntity.BlobPreviewMode.HEX -> BlobPreviewMode.HEX
                    SettingsEntity.BlobPreviewMode.BASE64 -> BlobPreviewMode.BASE_64
                    else -> BlobPreviewMode.PLACEHOLDER
                }
            )
        } ?: Settings()

    override suspend fun saveLinesLimit(input: SettingsParameters.LinesLimit) =
        linesLimit(input.isEnabled)

    override suspend fun saveLinesCount(input: SettingsParameters.LinesCount) =
        linesCount(input.count)

    override suspend fun saveTruncateMode(input: SettingsParameters.Truncate) =
        truncateMode(TruncateType(input.mode.ordinal)) // TODO: This needs to be in a mapper

    override suspend fun saveBlobPreview(input: SettingsParameters.BlobPreview) =
        blobPreviewMode(BlobPreviewType(input.mode.ordinal)) // TODO: This needs to be in a mapper
}
