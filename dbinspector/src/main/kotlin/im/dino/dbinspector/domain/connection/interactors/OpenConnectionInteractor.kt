package im.dino.dbinspector.domain.connection.interactors

import android.database.sqlite.SQLiteDatabase
import im.dino.dbinspector.data.Sources
import im.dino.dbinspector.domain.Interactors

internal class OpenConnectionInteractor(
    val source: Sources.Memory
) : Interactors.OpenConnection {

    override suspend fun invoke(input: String): SQLiteDatabase =
        source.openConnection(input)
}
