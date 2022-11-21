package com.infinum.dbinspector.domain.shared.models.parameters

import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.TruncateMode

internal sealed class SettingsParameters : BaseParameters {

    class Get : SettingsParameters()

    data class LinesLimit(
        val isEnabled: Boolean
    ) : SettingsParameters()

    data class LinesCount(
        val count: Int
    ) : SettingsParameters()

    data class Truncate(
        val mode: TruncateMode
    ) : SettingsParameters()

    data class BlobPreview(
        val mode: BlobPreviewMode
    ) : SettingsParameters()

    data class IgnoredTableName(
        val name: String
    ) : SettingsParameters()

    data class ServerPort(
        val port: String
    ) : SettingsParameters()
}
