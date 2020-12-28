package com.infinum.dbinspector.domain.shared.models.parameters

import android.text.TextUtils
import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.domain.shared.base.BaseParameters

internal sealed class SettingsParameters : BaseParameters {

    class Get : SettingsParameters()

    data class LinesLimit(
        val isEnabled: Boolean
    ) : SettingsParameters()

    data class LinesCount(
        val count: Int
    ) : SettingsParameters()

    data class TruncateMode(
        val mode: TextUtils.TruncateAt
    ) : SettingsParameters()

    data class BlobPreviewMode(
        val mode: BlobPreviewType
    ) : SettingsParameters()
}
