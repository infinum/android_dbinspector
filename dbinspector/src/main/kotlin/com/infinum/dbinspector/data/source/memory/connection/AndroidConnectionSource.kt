package com.infinum.dbinspector.data.source.memory.connection

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.data.Sources
import timber.log.Timber

internal class AndroidConnectionSource : Sources.Memory {

    private val connectionPool = hashMapOf<String, SQLiteDatabase>()

    override suspend fun openConnection(path: String): SQLiteDatabase {
        if (connectionPool.containsKey(path).not()) {
            connectionPool[path] = SQLiteDatabase.openOrCreateDatabase(path, null)
            Timber.i("Opened connection for $path")
        }
        Timber.i("Using connection for $path")
        return connectionPool[path] ?: SQLiteDatabase.openOrCreateDatabase(path, null)
    }

    override suspend fun closeConnection(path: String) {
        if (connectionPool.containsKey(path)) {
            connectionPool[path]?.let {
                if (it.isOpen) {
                    it.close()
                    connectionPool.remove(path)
                    Timber.i("Closed connection for $path")
                }
            }
        }
    }
}
