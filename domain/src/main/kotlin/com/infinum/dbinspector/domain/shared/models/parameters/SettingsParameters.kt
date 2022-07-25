package com.infinum.dbinspector.domain.shared.models.parameters

import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.TruncateMode

public sealed class SettingsParameters : BaseParameters {

    public class Get : SettingsParameters()

    public data class LinesLimit(
        val isEnabled: Boolean
    ) : SettingsParameters()

    public data class LinesCount(
        val count: Int
    ) : SettingsParameters()

    public data class Truncate(
        val mode: TruncateMode
    ) : SettingsParameters()

    public data class BlobPreview(
        val mode: BlobPreviewMode
    ) : SettingsParameters()

    public data class IgnoredTableName(
        val name: String
    ) : SettingsParameters()
}
