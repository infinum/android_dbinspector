package com.infinum.dbinspector.data.models.local.proto.input

import com.infinum.dbinspector.data.Data
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity

internal data class SettingsTask(
    val linesLimited: Boolean = false,
    val linesCount: Int = Data.Constants.Settings.LINES_LIMIT_MAXIMUM,
    val truncateMode: SettingsEntity.TruncateMode = SettingsEntity.TruncateMode.END,
    val blobPreviewMode: SettingsEntity.BlobPreviewMode = SettingsEntity.BlobPreviewMode.UTF8,
    val ignoredTableName: String? = null
)
