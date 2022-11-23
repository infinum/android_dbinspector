package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.server.Server

internal class StartServerInteractor(
    private val server: Server,
    private val dataStore: Sources.Local.Settings
) : Interactors.StartServer {

    override suspend fun invoke(input: SettingsTask): SettingsEntity {
        val result = server.start(input.serverPort.toInt())
        return if (result) {
            dataStore.store().updateData {
                it.toBuilder().setServerRunning(input.serverState).build()
            }
        } else {
            dataStore.current()
        }
    }
}
