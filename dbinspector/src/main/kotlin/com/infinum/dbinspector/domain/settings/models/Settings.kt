package com.infinum.dbinspector.domain.settings.models

import android.text.TextUtils
import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType

internal data class Settings(
    val linesLimitEnabled: Boolean = false,
    val linesCount: Int = 100,
    val truncateMode: TextUtils.TruncateAt = TextUtils.TruncateAt.END,
    val blobPreviewType: BlobPreviewType = BlobPreviewType.PLACEHOLDER
)
