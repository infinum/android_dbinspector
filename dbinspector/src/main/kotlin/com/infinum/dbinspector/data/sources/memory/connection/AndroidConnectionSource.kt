package com.infinum.dbinspector.data.sources.memory.connection

import android.database.sqlite.SQLiteDatabase
import androidx.annotation.VisibleForTesting
import com.infinum.dbinspector.data.Sources
import java.util.Collections

internal class AndroidConnectionSource : Sources.Memory.Connection {

    private val connectionPool: HashMap<String, SQLiteDatabase> = hashMapOf()
    @VisibleForTesting
    internal val syncPool: MutableMap<String, SQLiteDatabase> = Collections.synchronizedMap(connectionPool)

    override suspend fun openConnection(path: String): SQLiteDatabase {
        if (syncPool.containsKey(path).not()) {
            syncPool[path] = SQLiteDatabase.openOrCreateDatabase(path, null)
        }
        return syncPool[path] ?: SQLiteDatabase.openOrCreateDatabase(path, null)
    }

    override suspend fun closeConnection(path: String) {
        if (syncPool.containsKey(path)) {
            syncPool[path]?.let {
                if (it.isOpen) {
                    // Fix this multithreading problem
                    // it.close()
                    syncPool.remove(path)
                }
            }
        }
    }
}
