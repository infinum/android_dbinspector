package com.infinum.dbinspector.domain.raw.control

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Mappers

internal data class RawQueryControl(
    override val mapper: Mappers.RawQuery,
    override val converter: Converters.RawQuery
) : Control.RawQuery
