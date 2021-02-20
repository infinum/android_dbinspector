package com.infinum.dbinspector.domain.history.control

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Mappers

internal data class HistoryControl(
    override val mapper: Mappers.History,
    override val converter: Converters.History
) : Control.History
