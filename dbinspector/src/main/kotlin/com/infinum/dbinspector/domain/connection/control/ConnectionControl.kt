package com.infinum.dbinspector.domain.connection.control

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Mappers

internal data class ConnectionControl(
    override val mapper: Mappers.Connection,
    override val converter: Converters.Connection
) : Control.Connection
