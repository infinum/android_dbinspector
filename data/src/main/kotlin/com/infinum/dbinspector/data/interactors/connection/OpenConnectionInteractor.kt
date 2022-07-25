package com.infinum.dbinspector.data.interactors.connection

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.Interactors

internal class OpenConnectionInteractor(
    private val source: Sources.Memory.Connection
) : Interactors.OpenConnection {

    override suspend fun invoke(input: String): SQLiteDatabase =
        source.openConnection(input)
}
