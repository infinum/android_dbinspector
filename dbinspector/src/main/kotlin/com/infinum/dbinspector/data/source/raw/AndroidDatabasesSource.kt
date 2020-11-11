package com.infinum.dbinspector.data.source.raw

import com.infinum.dbinspector.R
import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.extensions.databaseDir
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileFilter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class AndroidDatabasesSource : Sources.Raw {

    override suspend fun getDatabases(operation: Operation): List<File> =
        suspendCancellableCoroutine {
            try {
                val allowed = operation.context.resources.getStringArray(R.array.dbinspector_allowed).toList()
                val ignored = operation.context.resources.getStringArray(R.array.dbinspector_ignored).toList()

                // look for standard sqlite databases in the databases dir
                val databases: MutableSet<File> = operation.context.databaseList()
                    .filter { name -> ignored.none { extension -> name.endsWith(extension) } }
                    .map { path -> operation.context.getDatabasePath(path) }
                    .toMutableSet()

                val filter = FileFilter { file ->
                    (file.isFile && file.canRead() && (file.extension in allowed))
                }
                // CouchBase Lite stores the databases in the app files dir
                // we get all files recursively because .cblite2 is a dir with the actual sqlite db
                getFiles(operation.context.filesDir)
                    .filter { file -> filter.accept(file) }
                    .forEach { file ->
                        databases.add(file)
                    }

                it.resume(databases.toList())
            } catch (exception: IOException) {
                it.resumeWithException(exception)
            }
        }

    override suspend fun importDatabases(operation: Operation): List<File> =
        suspendCancellableCoroutine {
            try {
                val importedDatabases: MutableSet<File> = mutableSetOf()

                operation.importUris.forEach { uri ->
                    uri.lastPathSegment?.split("/")?.last()?.let { filename ->
                        operation.context.contentResolver.openInputStream(uri)?.use { inputStream ->
                            val file = File(operation.context.databaseDir, filename)
                            FileOutputStream(file).use { outputStream ->
                                outputStream.write(inputStream.readBytes())
                            }
                            importedDatabases.add(file)
                        }
                    }
                }

                it.resume(importedDatabases.toList())
            } catch (exception: FileNotFoundException) {
                it.resumeWithException(exception)
            } catch (exception: SecurityException) {
                it.resumeWithException(exception)
            }
        }

    override suspend fun removeDatabase(operation: Operation): List<File> =
        suspendCancellableCoroutine {
            try {
                val ok = operation.context.deleteDatabase(
                    "${operation.databaseDescriptor?.name.orEmpty()}." +
                        operation.databaseDescriptor?.extension.orEmpty()
                )

                if (ok) {
                    it.resume(listOf(File(operation.databaseDescriptor?.absolutePath.orEmpty())))
                } else {
                    throw IOException("Cannot delete database.")
                }
            } catch (exception: IOException) {
                it.resumeWithException(exception)
            }
        }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun renameDatabase(operation: Operation): List<File> =
        suspendCancellableCoroutine {
            try {
                val originalFile = File(operation.databaseDescriptor?.absolutePath.orEmpty())
                val destinationFile = File(
                    "${operation.databaseDescriptor?.parentPath.orEmpty()}/" +
                        "${operation.argument.orEmpty()}." +
                        operation.databaseDescriptor?.extension.orEmpty()
                )

                val ok = originalFile.renameTo(destinationFile)

                if (ok) {
                    it.resume(listOf(destinationFile))
                } else {
                    throw IOException("Cannot rename database.")
                }
            } catch (exception: NullPointerException) {
                it.resumeWithException(exception)
            } catch (exception: SecurityException) {
                it.resumeWithException(exception)
            } catch (exception: IOException) {
                it.resumeWithException(exception)
            }
        }

    override suspend fun copyDatabase(operation: Operation): List<File> =
        suspendCancellableCoroutine {
            try {
                var counter = 1
                var fileName = "${operation.databaseDescriptor?.parentPath.orEmpty()}/" +
                    "${operation.databaseDescriptor?.name.orEmpty()}_$counter." +
                    operation.databaseDescriptor?.extension.orEmpty()

                var targetFile = File(fileName)
                while (targetFile.exists()) {
                    fileName = "${operation.databaseDescriptor?.parentPath.orEmpty()}/" +
                        "${operation.databaseDescriptor?.name.orEmpty()}_$counter." +
                        operation.databaseDescriptor?.extension.orEmpty()

                    targetFile = File(fileName)
                    counter++
                }

                val newFile = File(operation.databaseDescriptor?.absolutePath.orEmpty())
                    .copyTo(target = targetFile, overwrite = true)

                if (newFile.exists()) {
                    it.resume(listOf(newFile))
                } else {
                    throw IOException("Cannot copy database.")
                }
            } catch (exception: NoSuchFileException) {
                it.resumeWithException(exception)
            } catch (exception: FileAlreadyExistsException) {
                it.resumeWithException(exception)
            } catch (exception: IOException) {
                it.resumeWithException(exception)
            }
        }

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
