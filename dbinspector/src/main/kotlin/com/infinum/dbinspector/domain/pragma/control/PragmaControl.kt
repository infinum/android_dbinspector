package com.infinum.dbinspector.domain.pragma.control

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Mappers

internal data class PragmaControl(
    override val mapper: Mappers.Pragma,
    override val converter: Converters.Pragma
) : Control.Pragma
