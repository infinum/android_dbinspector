package im.dino.dbinspector.data.source.memory

import android.database.sqlite.SQLiteDatabase
import im.dino.dbinspector.BuildConfig
import im.dino.dbinspector.data.Sources

internal class AndroidConnectionSource : Sources.Memory {

    private val connectionPool = hashMapOf<String, SQLiteDatabase>()

    override suspend fun openConnection(path: String): SQLiteDatabase {
        if (connectionPool.containsKey(path).not()) {
            connectionPool[path] = SQLiteDatabase.openOrCreateDatabase(path, null)
            if (BuildConfig.DEBUG) {
                println("Opened connection for $path")
            }
        }
        if (BuildConfig.DEBUG) {
            println("Using connection for $path")
        }
        return connectionPool[path] ?: SQLiteDatabase.openOrCreateDatabase(path, null)
    }

    override suspend fun closeConnection(path: String) {
        if (connectionPool.containsKey(path)) {
            connectionPool[path]?.let {
                if (it.isOpen) {
                    it.close()
                    connectionPool.remove(path)
                    if (BuildConfig.DEBUG) {
                        println("Closed connection for $path")
                    }
                }
            }
        }
    }
}
