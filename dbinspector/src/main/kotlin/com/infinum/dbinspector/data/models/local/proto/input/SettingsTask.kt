package com.infinum.dbinspector.data.models.local.proto.input

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.ui.shared.Constants

data class SettingsTask(
    val linesLimited: Boolean = false,
    val linesCount: Int = Constants.Settings.LINES_LIMIT_MAXIMUM,
    val truncateMode: SettingsEntity.TruncateMode = SettingsEntity.TruncateMode.END,
    val blobPreviewMode: SettingsEntity.BlobPreviewMode = SettingsEntity.BlobPreviewMode.UTF8
)
