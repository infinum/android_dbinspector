package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.server.Server

internal class SaveServerPortInteractor(
    private val server: Server,
    private val dataStore: Sources.Local.Settings
) : Interactors.SaveServerPort {

    override suspend fun invoke(input: SettingsTask): SettingsEntity {
        val current = dataStore.current()
        return if (current.serverRunning) {
            val stopResult = server.stop()
            if (stopResult) {
                val newEntity = dataStore.store().updateData {
                    it.toBuilder()
                        .setServerPort(input.serverPort)
                        .build()
                }
                val startResult = server.start(newEntity.serverPort.toInt())
                if (startResult) {
                    newEntity
                } else {
                    current
                }
            } else {
                current
            }
        } else {
            dataStore.store().updateData {
                it.toBuilder()
                    .setServerPort(input.serverPort)
                    .build()
            }
        }
    }
}
