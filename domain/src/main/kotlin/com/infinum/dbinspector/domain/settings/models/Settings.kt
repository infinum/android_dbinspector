package com.infinum.dbinspector.domain.settings.models

import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.TruncateMode

public data class Settings(
    val linesLimitEnabled: Boolean = false,
    val linesCount: Int = 100,
    val truncateMode: TruncateMode = TruncateMode.END,
    val blobPreviewMode: BlobPreviewMode = BlobPreviewMode.PLACEHOLDER,
    val ignoredTableNames: List<String> = listOf()
)
