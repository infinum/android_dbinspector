package com.infinum.dbinspector.web

import android.content.Context
import com.infinum.dbinspector.web.extensions.readAll
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import org.json.JSONObject

internal object DbInspectorWeb {

    // randomize and anonymize (application package name to sha1?
    private const val rootDir: String = "web"

    private const val buildDate: String = "build.date.json"
    private const val keyTimestamp: String = "timestamp"

    fun deploy(context: Context) {
        val root = File("${context.filesDir.absolutePath}${File.separator}$rootDir")
        if (root.exists().not()) {
            root.mkdirs()
            copyWebResources(context, rootDir, root)
        } else {
            val diskFile = File(root, buildDate)
            val diskJson: String = diskFile.readAll()
            if (diskFile.exists()) {
                val assetsJson: String = context.assets
                    .open("$rootDir${File.separator}$buildDate")
                    .readAll()

                val diskTimestamp: Long = JSONObject(diskJson).getLong(keyTimestamp)
                val assetsTimestamp: Long = JSONObject(assetsJson).getLong(keyTimestamp)

                if (assetsTimestamp > diskTimestamp) {
                    copyWebResources(context, rootDir, root)
                }
            } else {
                copyWebResources(context, rootDir, root)
            }
        }
    }

    private fun copyWebResources(context: Context, assetDir: String, outDir: File) {
        val files = context.assets.list(assetDir) ?: return

        for (path in files) {
            val outFile = File(outDir, path)
            val assetPath = "$assetDir${File.separator}$path"

            val input = try {
                context.assets.open(assetPath).buffered()
            } catch (_: FileNotFoundException) {
                outFile.mkdir()
                copyWebResources(context, assetPath, File(outDir, path))
                continue
            }

            FileOutputStream(outFile).use { stream ->
                stream.write(input.readBytes())
            }

            input.close()
        }
    }
}
