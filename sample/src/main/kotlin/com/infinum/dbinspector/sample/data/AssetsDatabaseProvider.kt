package com.infinum.dbinspector.sample.data

import android.content.Context
import java.io.File
import java.io.FileOutputStream

class AssetsDatabaseProvider(
    private val context: Context
) : DatabaseProvider {

    private var databasesDir = context.filesDir.path

    init {
        databasesDir = databasesDir.substring(0, databasesDir.lastIndexOf("/")) + "/databases"

        File(databasesDir).let {
            if (it.exists().not()) {
                it.mkdir()
            }
        }
    }

    override fun names(): List<String> =
        listOf(
            "blog.db",
            "chinook.db",
            "northwind.sqlite"
        )

    override fun copy() =
        names()
            .filterNot { File(databasesDir, it).exists() }
            .forEach { filename ->
                context.assets.open("databases/$filename").use { inputStream ->
                    FileOutputStream(File(databasesDir, filename))
                        .use { outputStream ->
                            outputStream.write(inputStream.readBytes())
                        }
                }
            }
}