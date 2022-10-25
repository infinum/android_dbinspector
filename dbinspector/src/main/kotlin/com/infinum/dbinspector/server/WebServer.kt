package com.infinum.dbinspector.server

import android.content.Context
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.files
import io.ktor.server.http.content.static
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import org.slf4j.event.Level

internal class WebServer(
    private val context: Context
) {

    private var currentServer: NettyApplicationEngine? = null

    fun start() {
        if (currentServer == null) {
            val webPath = "${context.filesDir.absolutePath}${File.separator}web"
            val webDir = File(webPath)
            if (webDir.exists().not()) {
                webDir.mkdirs()
                copyWebResources("web", webDir)
            }

            val server = embeddedServer(Netty, port = 8080) {
                install(CallLogging) {
                    level = Level.INFO
                }
                install(StatusPages) {
                    exception(Throwable::class) { call, throwable ->
                        call.respondText(throwable.localizedMessage.orEmpty(), ContentType.Text.Plain, HttpStatusCode.InternalServerError)
                    }
                }
                install(ContentNegotiation) {
                    json()
                }

                /*
            install(CORS) {
                anyHost()
                header(HttpHeaders.ContentType)
                header(HttpHeaders.AcceptLanguage)
                header(HttpHeaders.Accept)
                header(HttpHeaders.ContentLanguage)
                header(HttpHeaders.AcceptCharset)
                header(HttpHeaders.AcceptEncoding)
                header(HttpHeaders.AccessControlAllowOrigin)
            }
             */

                routing {
                    // TODO: Design a landing page with showcase and call to action to list /databases
                    static("/") {
                        files(webDir)
                    }
                    databases(context)
                }
            }.start(wait = false)
            currentServer = server
        }
    }

    fun stop() {
        currentServer?.stop(100L, 100L)
        currentServer = null
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
