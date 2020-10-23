package com.infinum.dbinspector.domain.connection.interactors

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.domain.Interactors

internal class OpenConnectionInteractor(
    val source: Sources.Memory
) : Interactors.OpenConnection {

    override suspend fun invoke(input: String): SQLiteDatabase =
        source.openConnection(input)
}
