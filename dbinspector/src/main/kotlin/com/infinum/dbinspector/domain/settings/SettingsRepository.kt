package com.infinum.dbinspector.domain.settings

import android.text.TextUtils
import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.ui.shared.Constants

internal class SettingsRepository(
    private val loadAll: Interactors.LoadSettings,
    private val linesLimit: Interactors.SaveLinesLimit,
    private val linesCount: Interactors.SaveLinesCount,
    private val truncateMode: Interactors.SaveTruncateMode,
    private val blobPreviewMode: Interactors.SaveBlobPreviewMode,
) : Repositories.Settings {

    override suspend fun load(): Settings =
        loadAll(Unit)?.let {
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
                blobPreviewType = when (it.blobPreview) {
                    SettingsEntity.BlobPreviewMode.PLACEHOLDER -> BlobPreviewType.PLACEHOLDER
                    SettingsEntity.BlobPreviewMode.UTF8 -> BlobPreviewType.UTF_8
                    SettingsEntity.BlobPreviewMode.HEX -> BlobPreviewType.HEX
                    SettingsEntity.BlobPreviewMode.BASE64 -> BlobPreviewType.BASE_64
                    else -> BlobPreviewType.PLACEHOLDER
                }
            )
        } ?: Settings()

    override suspend fun saveLinesLimit(isEnabled: Boolean) =
        linesLimit(isEnabled)

    override suspend fun saveLinesCount(count: Int) =
        linesCount(count)

    override suspend fun saveTruncateMode(mode: TextUtils.TruncateAt) =
        truncateMode(mode)

    override suspend fun saveBlobPreview(mode: BlobPreviewType) =
        blobPreviewMode(mode)
}
