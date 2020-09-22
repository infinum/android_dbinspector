package im.dino.dbinspector.data.source.local

import android.content.Context
import im.dino.dbinspector.ui.shared.Constants
import java.io.File
import java.io.FileFilter
import java.util.ArrayList

/**
 * Created by dino on 23/02/14.
 */
object DatabaseFinder {

    private lateinit var context: Context

    private var databases: MutableSet<File> = mutableSetOf()

    fun initialise(context: Context) {
        this.context = context
    }

    fun get(): List<File> = databases.toList()

    fun find() {
        // look for standard sqlite databases in the databases dir
        databases = context.databaseList()
            .filter { name -> Constants.Extensions.IGNORED.none { name.endsWith(it) } }
            .map { context.getDatabasePath(it) }
            .toMutableSet()

        val filter = FileFilter { file ->
            (file.isFile && file.canRead() && (file.extension in Constants.Extensions.ALLOWED))
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