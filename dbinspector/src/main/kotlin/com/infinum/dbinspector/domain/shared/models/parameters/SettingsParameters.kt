package com.infinum.dbinspector.domain.shared.models.parameters

import android.text.TextUtils
import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode

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

    data class BlobPreview(
        val mode: BlobPreviewMode
    ) : SettingsParameters()
}
