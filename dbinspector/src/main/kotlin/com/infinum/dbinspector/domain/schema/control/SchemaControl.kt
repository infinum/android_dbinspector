package com.infinum.dbinspector.domain.schema.control

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Mappers

internal data class SchemaControl(
    override val mapper: Mappers.Schema,
    override val converter: Converters.Schema
) : Control.Schema
