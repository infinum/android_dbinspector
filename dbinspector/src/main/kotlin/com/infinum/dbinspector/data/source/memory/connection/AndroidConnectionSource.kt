package com.infinum.dbinspector.data.source.memory.connection

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.data.Sources

internal class AndroidConnectionSource : Sources.Memory {

    private val connectionPool = hashMapOf<String, SQLiteDatabase>()

    override suspend fun openConnection(path: String): SQLiteDatabase {
        if (connectionPool.containsKey(path).not()) {
            connectionPool[path] = SQLiteDatabase.openOrCreateDatabase(path, null)
        }
        return connectionPool[path] ?: SQLiteDatabase.openOrCreateDatabase(path, null)
    }

    override suspend fun closeConnection(path: String) {
        if (connectionPool.containsKey(path)) {
            connectionPool[path]?.let {
                if (it.isOpen) {
                    it.close()
                    connectionPool.remove(path)
                }
            }
        }
    }
}
