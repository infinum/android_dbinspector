package com.infinum.dbinspector.domain.connection.control.mappers

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.connection.models.DatabaseConnection

internal class ConnectionMapper : Mappers.Connection {

    override suspend fun invoke(model: SQLiteDatabase): DatabaseConnection =
        DatabaseConnection(database = model)
}
