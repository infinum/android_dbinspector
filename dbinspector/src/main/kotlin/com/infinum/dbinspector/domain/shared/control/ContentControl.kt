package com.infinum.dbinspector.domain.shared.control

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Mappers

internal data class ContentControl(
    override val mapper: Mappers.Content,
    override val converter: Converters.Content
) : Control.Content
