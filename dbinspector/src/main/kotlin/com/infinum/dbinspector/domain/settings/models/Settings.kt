package com.infinum.dbinspector.domain.settings.models

import android.text.TextUtils
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode

internal data class Settings(
    val linesLimitEnabled: Boolean = false,
    val linesCount: Int = 100,
    val truncateMode: TextUtils.TruncateAt = TextUtils.TruncateAt.END,
    val blobPreviewMode: BlobPreviewMode = BlobPreviewMode.PLACEHOLDER
)
