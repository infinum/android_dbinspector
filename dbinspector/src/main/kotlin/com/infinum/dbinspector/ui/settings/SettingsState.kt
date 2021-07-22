package com.infinum.dbinspector.ui.settings

import com.infinum.dbinspector.domain.settings.models.Settings as SettingsModel

internal sealed class SettingsState {

    data class Settings(
        val settings: SettingsModel
    ) : SettingsState()
}
