package com.infinum.dbinspector.domain.connection

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories

internal class ConnectionRepository(
    private val openInteractor: Interactors.OpenConnection,
    private val closeInteractor: Interactors.CloseConnection,
) : Repositories.Connection {

    override suspend fun open(path: String): SQLiteDatabase =
        openInteractor(path)

    override suspend fun close(path: String) =
        closeInteractor(path)
}
