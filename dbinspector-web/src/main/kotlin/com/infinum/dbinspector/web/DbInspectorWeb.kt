package com.infinum.dbinspector.web

import android.content.Context
import com.infinum.dbinspector.web.extensions.readAll
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.angular
import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import org.json.JSONObject
import org.slf4j.event.Level

internal object DbInspectorWeb {

    private const val port: Int = 3800

    //randomize and anonymize (application packagename to sha1?
    private const val rootDir: String = "web"

    private const val buildDate: String = "build.date.json"
    private const val keyTimestamp: String = "timestamp"

    private var currentServer: NettyApplicationEngine? = null

    fun serve(context: Context) {
        val root = File("${context.filesDir.absolutePath}${File.separator}$rootDir")

        if (currentServer == null) {

            deploy(context, root)

            val server = embeddedServer(Netty, port = port) {
                install(CallLogging) {
                    level = Level.INFO
                }
                install(StatusPages) {
                    exception(Throwable::class) { call, throwable ->
                        call.respondText(
                            throwable.stackTraceToString(),
                            ContentType.Text.Plain,
                            HttpStatusCode.InternalServerError
                        )
                    }
                }
                install(CORS) {
                    allowMethod(HttpMethod.Patch)
                    allowMethod(HttpMethod.Delete)
                    allowSameOrigin = true
                    allowNonSimpleContentTypes = true
                    anyHost()
                }

                routing {
                    singlePageApplication {
                        angular(root.absolutePath)
                    }
                }
            }.start(wait = false)

            currentServer = server
        }
    }

    private fun deploy(context: Context, root: File) {
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