package com.infinum.dbinspector.server

import android.content.Context
import com.infinum.dbinspector.server.controllers.ContentController
import com.infinum.dbinspector.server.controllers.DatabaseController
import com.infinum.dbinspector.server.controllers.PragmaController
import com.infinum.dbinspector.server.controllers.SchemaController
import com.infinum.dbinspector.server.routes.content
import com.infinum.dbinspector.server.routes.databases
import com.infinum.dbinspector.server.routes.pragma
import com.infinum.dbinspector.server.routes.schema
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
import org.slf4j.event.Level

internal class WebServer(
    private val context: Context
) : Server {

    internal companion object {
        private const val VERSION = 1
        private const val gracePeriodMillis: Long = 100L
        private const val timeoutMillis: Long = 100L
        private const val rootDir: String = "web"
    }

    private var currentServer: NettyApplicationEngine? = null

    private val angularApp = File("${context.filesDir.absolutePath}${File.separator}$rootDir")

    @Suppress("KotlinConstantConditions")
    override fun start(port: Int): Boolean {
        if (currentServer == null) {
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
                    if (angularApp.exists()) {
                        singlePageApplication {
                            angular(angularApp.absolutePath)
                        }
                    }
                    databases(VERSION, DatabaseController(context))
                    schema(VERSION, SchemaController(context))
                    content(VERSION, ContentController(context))
                    pragma(VERSION, PragmaController(context))
                }
            }.start(wait = false)

            currentServer = server

            return currentServer != null
        } else {
            return false
        }
    }

    @Suppress("KotlinConstantConditions")
    override fun stop(): Boolean =
        if (currentServer != null) {
            currentServer?.stop(gracePeriodMillis, timeoutMillis)
            currentServer = null
            currentServer == null
        } else {
            false
        }
}
