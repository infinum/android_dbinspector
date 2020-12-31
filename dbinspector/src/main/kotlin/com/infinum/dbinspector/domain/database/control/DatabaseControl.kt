package com.infinum.dbinspector.domain.database.control

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Mappers

internal data class DatabaseControl(
    override val mapper: Mappers.DatabaseDescriptor,
    override val converter: Converters.Database
) : Control.Database
