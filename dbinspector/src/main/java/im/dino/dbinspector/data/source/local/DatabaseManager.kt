package im.dino.dbinspector.data.source.local

import android.content.Context
import android.net.Uri
import im.dino.dbinspector.R
import im.dino.dbinspector.extensions.databaseDir
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileFilter
import java.io.FileOutputStream
import java.util.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Created by dino on 23/02/14.
 */
internal object DatabaseManager {

    private lateinit var context: Context

    private var allowed: List<String> = listOf()
    private var ignored: List<String> = listOf()

    fun initialise(context: Context) {
        this.context = context

        allowed = context.resources.getStringArray(R.array.dbinspector_allowed).toList()
        ignored = context.resources.getStringArray(R.array.dbinspector_ignored).toList()
    }

    suspend fun find(): MutableSet<File> =
        suspendCancellableCoroutine {
            try {
                // look for standard sqlite databases in the databases dir
                val databases: MutableSet<File> = context.databaseList()
                    .filter { name -> ignored.none { name.endsWith(it) } }
                    .map { context.getDatabasePath(it) }
                    .toMutableSet()

                val filter = FileFilter { file ->
                    (file.isFile && file.canRead() && (file.extension in allowed))
                }
                // CouchBase Lite stores the databases in the app files dir
                // we get all files recursively because .cblite2 is a dir with the actual sqlite db
                getFiles(context.filesDir)
                    .filter { filter.accept(it) }
                    .forEach {
                        databases.add(it)
                    }
                it.resume(databases)
            } catch (exception: Exception) {
                it.resumeWithException(exception)
            }
        }

    suspend fun import(uris: List<Uri>): Unit =
        suspendCancellableCoroutine {
            try {
                uris.forEach { uri ->
                    uri.lastPathSegment?.split("/")?.last()?.let { filename ->
                        context.contentResolver.openInputStream(uri)?.use { inputStream ->
                            FileOutputStream(File(context.databaseDir, filename))
                                .use { outputStream ->
                                    outputStream.write(inputStream.readBytes())
                                }
                        }
                    }
                }
                it.resume(Unit)
            } catch (exception: Exception) {
                it.resumeWithException(exception)
            }
        }

    suspend fun remove(name: String): Boolean =
        suspendCancellableCoroutine {
            try {
                val ok = context.deleteDatabase(name)
                it.resume(ok)
            } catch (exception: Exception) {
                it.resumeWithException(exception)
            }
        }


    suspend fun rename(databasePath: String, databaseFilename: String): Boolean =
        suspendCancellableCoroutine {
            try {
                val ok = File(databasePath)
                    .renameTo(
                        File(databaseFilename)
                    )
                it.resume(ok)
            } catch (exception: Exception) {
                it.resumeWithException(exception)
            }
        }

    suspend fun copy(databaseAbsolutePath: String, databasePath: String, databaseName: String, databaseExtension: String): Boolean =
        suspendCancellableCoroutine {
            try {
                var counter = 1
                var fileName = "$databasePath/${databaseName}_$counter.$databaseExtension"

                var targetFile = File(fileName)
                while (targetFile.exists()) {
                    fileName = "$databasePath/${databaseName}_$counter.$databaseExtension"
                    targetFile = File(fileName)
                    counter++
                }

                val newFile = File(databaseAbsolutePath).copyTo(target = targetFile, overwrite = true)
                it.resume(newFile.exists())
            } catch (exception: Exception) {
                it.resumeWithException(exception)
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