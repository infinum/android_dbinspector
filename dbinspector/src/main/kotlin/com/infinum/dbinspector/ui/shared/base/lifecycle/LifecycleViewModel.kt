package com.infinum.dbinspector.ui.shared.base.lifecycle

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.ui.shared.base.BaseViewModel

internal abstract class LifecycleViewModel<State, Event>(
    private val openConnection: UseCases.OpenConnection,
    private val closeConnection: UseCases.CloseConnection
) : BaseViewModel<State, Event>() {

    lateinit var databasePath: String

    fun open() =
        launch {
            io {
                openConnection(ConnectionParameters(databasePath))
            }
        }

    fun close() =
        launch {
            io {
                closeConnection(ConnectionParameters(databasePath))
            }
        }
}
