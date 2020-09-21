package im.dino.dbinspector.data.source.local

import android.content.Context
import java.io.File
import java.io.FileFilter
import java.util.ArrayList
import java.util.HashSet

/**
 * Created by dino on 23/02/14.
 */
object DatabaseHelper {

    fun getDatabaseList(context: Context): List<File> {
        val databases: MutableSet<File> = HashSet()

        // look for standard sqlite databases in the databases dir
        val contextDatabases = context.databaseList()
        for (database in contextDatabases) {
            // don't show various sqlite-internal file databases
            // see https://www.sqlite.org/tempfiles.html
            if (!database.endsWith("-journal") &&  //  they only hold temporary rollback data
                !database.endsWith("-wal") &&  // write-ahead log for WAL modes
                !database.endsWith("-shm") // shared-memory files in WAL mode with multiple connections
            ) {
                databases.add(context.getDatabasePath(database))
            }
        }
        val dbFileFilter = FileFilter { file ->
            val filename = file.name
            (file.isFile && file.canRead()
                && (filename.endsWith(".sql")
                || filename.endsWith(".sqlite")
                || filename.endsWith(".sqlite3")
                || filename.endsWith(".db")
                || filename.endsWith(".cblite")
                || filename.endsWith(".cblite2")))
        }

        // CouchBase Lite stores the databases in the app files dir
        // we get all files recursively because .cblite2 is a dir with the actual sqlite db
        val internalStorageFiles = getFiles(context.filesDir)
        for (internalFile in internalStorageFiles) {
            if (dbFileFilter.accept(internalFile)) {
                databases.add(internalFile)
            }
        }
        return ArrayList(databases)
    }

    /**
     * Recursively builds a list of all the files in the directory and subdirectories.
     */
    private fun getFiles(filesDir: File): List<File> {
        val files: MutableList<File> = ArrayList()
        findFiles(filesDir, files)
        return files
    }

    private fun findFiles(file: File, fileList: MutableList<File>) {
        if (file.isFile && file.canRead()) {
            fileList.add(file)
        } else if (file.isDirectory) {
            for (fileInDir in file.listFiles()) {
                findFiles(fileInDir, fileList)
            }
        }
    }
}