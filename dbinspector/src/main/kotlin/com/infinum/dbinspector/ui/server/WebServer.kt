package com.infinum.dbinspector.ui.server

import android.content.Context
import com.infinum.dbinspector.ui.server.controllers.ContentController
import com.infinum.dbinspector.ui.server.controllers.DatabaseController
import com.infinum.dbinspector.ui.server.controllers.PragmaController
import com.infinum.dbinspector.ui.server.controllers.SchemaController
import com.infinum.dbinspector.ui.server.routes.content
import com.infinum.dbinspector.ui.server.routes.databases
import com.infinum.dbinspector.ui.server.routes.pragma
import com.infinum.dbinspector.ui.server.routes.schema
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.angular
import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import org.json.JSONObject
import org.slf4j.event.Level

internal class WebServer(
    private val context: Context,
    private val port: Int = 3700,
    private val autoStart: Boolean = false
) {

    internal companion object {
        private const val VERSION = 1
    }

    private var currentServer: NettyApplicationEngine? = null

    init {
        if (autoStart) {
            start()
        }
    }

    fun start() {
        if (currentServer == null) {
            val webPath = "${context.filesDir.absolutePath}${File.separator}web"
            val webDir = File(webPath)

            deploy(webDir)

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
                install(ContentNegotiation) {
                    json()
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
                        angular(webDir.absolutePath)
                    }
                    databases(VERSION, DatabaseController(context))
                    schema(VERSION, SchemaController(context))
                    content(VERSION, ContentController(context))
                    pragma(VERSION, PragmaController(context))
                }
            }.start(wait = false)
            currentServer = server
        }
    }

    fun stop() {
        currentServer?.stop(100L, 100L)
        currentServer = null
    }

    private fun deploy(webDir: File) {
        if (webDir.exists().not()) {
            webDir.mkdirs()
            copyWebResources("web", webDir)
        } else {
            val filesJsonFile = File(webDir, "build.date.json")
            val filesJson: String = filesJsonFile
                .bufferedReader()
                .use { it.readText() }
            if (filesJsonFile.exists()) {
                val assetsJson: String = context.assets.open("web/build.date.json")
                    .bufferedReader()
                    .use { it.readText() }

                val filesTimestamp: Long = JSONObject(filesJson).getLong("timestamp")
                val assetsTimestamp: Long = JSONObject(assetsJson).getLong("timestamp")

                if (assetsTimestamp > filesTimestamp) {
                    copyWebResources("web", webDir)
                }
            } else {
                copyWebResources("web", webDir)
            }
        }
    }

    private fun copyWebResources(assetDir: String, outDir: File) {
        val files = context.assets.list(assetDir) ?: return

        for (path in files) {
            val outFile = File(outDir, path)
            val assetPath = "$assetDir/$path"

            val input = try {
                context.assets.open(assetPath).buffered()
            } catch (_: FileNotFoundException) {
                // Seems like path points to a directory
                outFile.mkdir()
                copyWebResources(assetPath, File(outDir, path))
                continue
            }

            FileOutputStream(outFile).use { stream ->
                stream.write(input.readBytes())
            }

            input.close()
        }
    }
}
