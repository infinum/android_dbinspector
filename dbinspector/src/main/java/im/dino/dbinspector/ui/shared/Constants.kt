package im.dino.dbinspector.ui.shared

object Constants {

    object Keys {

        const val DATABASE_PATH = "KEY_DATABASE_PATH"
        const val DATABASE = "KEY_DATABASE"
        const val DATABASE_NAME = "KEY_DATABASE_NAME"
        const val TABLE = "KEY_TABLE"
        const val TABLE_NAME = "KEY_TABLE_NAME"
    }

    object Extensions {

        val ALLOWED = listOf(
            "sql",
            "sqlite",
            "sqlite3",
            "db",
            "cblite",
            "cblite2"
        )

        // don't show various sqlite-internal file databases
        // see https://www.sqlite.org/tempfiles.html
        val IGNORED = listOf(
            "-journal", //  they only hold temporary rollback data
            "-wal", // write-ahead log for WAL modes
            "-shm" // shared-memory files in WAL mode with multiple connections
        )
    }
}