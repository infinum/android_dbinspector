package com.infinum.dbinspector.domain.settings.control

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Mappers

internal data class SettingsControl(
    override val mapper: Mappers.Settings,
    override val converter: Converters.Settings
) : Control.Settings
