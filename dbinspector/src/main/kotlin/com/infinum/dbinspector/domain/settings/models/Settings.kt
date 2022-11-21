package com.infinum.dbinspector.domain.settings.models

import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.TruncateMode

internal data class Settings(
    val linesLimitEnabled: Boolean = false,
    val linesCount: Int = Domain.Constants.Settings.LINES_LIMIT_MAXIMUM,
    val truncateMode: TruncateMode = TruncateMode.END,
    val blobPreviewMode: BlobPreviewMode = BlobPreviewMode.PLACEHOLDER,
    val ignoredTableNames: List<String> = listOf(),
    val serverRunning: Boolean = false,
    val serverPort: String = ""
)
