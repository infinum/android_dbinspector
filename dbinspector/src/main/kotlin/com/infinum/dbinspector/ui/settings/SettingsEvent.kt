package com.infinum.dbinspector.ui.settings

internal sealed class SettingsEvent {

    data class AddIgnoredTable(
        val name: String
    ) : SettingsEvent()

    data class RemoveIgnoredTable(
        val name: String
    ) : SettingsEvent()

    class ServerStarted : SettingsEvent()

    class ServerStopped : SettingsEvent()
}
