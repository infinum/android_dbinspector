package im.dino.dbinspector.data.source.local

import android.content.Context
import java.io.File
import java.io.FileFilter
import java.util.ArrayList

/**
 * Created by dino on 23/02/14.
 */
object DatabaseFinder {

    private val includeExtensions = listOf(
        "sql",
        "sqlite",
        "sqlite3",
        "db",
        "cblite",
        "cblite2"
    )

    // don't show various sqlite-internal file databases
    // see https://www.sqlite.org/tempfiles.html
    private val ignoredFiles = listOf(
        "-journal", //  they only hold temporary rollback data
        "-wal", // write-ahead log for WAL modes
        "-shm" // shared-memory files in WAL mode with multiple connections
    )

    private var databases: MutableSet<File> = mutableSetOf()

    fun get(): List<File> = databases.toList()

    fun find(context: Context) {
        // look for standard sqlite databases in the databases dir
        databases = context.databaseList()
            .filter { name -> ignoredFiles.none { name.endsWith(it) } }
            .map { context.getDatabasePath(it) }
            .toMutableSet()

        val filter = FileFilter { file ->
            (file.isFile && file.canRead() && (file.extension in includeExtensions))
        }
        // CouchBase Lite stores the databases in the app files dir
        // we get all files recursively because .cblite2 is a dir with the actual sqlite db
        getFiles(context.filesDir)
            .filter { filter.accept(it) }
            .forEach {
                databases.add(it)
            }
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
            for (fileInDir in file.listFiles().orEmpty()) {
                findFiles(fileInDir, fileList)
            }
        }
    }
}