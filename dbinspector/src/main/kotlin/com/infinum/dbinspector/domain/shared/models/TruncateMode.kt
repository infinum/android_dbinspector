package com.infinum.dbinspector.domain.shared.models

import com.infinum.dbinspector.domain.shared.base.BaseParameters

// TODO: Should this inherit BaseParameters???
internal enum class TruncateMode : BaseParameters {
    START,
    MIDDLE,
    END
}
