package com.infinum.dbinspector.domain.connection

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters

internal class ConnectionRepository(
    private val openInteractor: Interactors.OpenConnection,
    private val closeInteractor: Interactors.CloseConnection,
) : Repositories.Connection {

    override suspend fun open(input: ConnectionParameters): SQLiteDatabase =
        openInteractor(input.databasePath)

    override suspend fun close(input: ConnectionParameters) =
        closeInteractor(input.databasePath)
}
