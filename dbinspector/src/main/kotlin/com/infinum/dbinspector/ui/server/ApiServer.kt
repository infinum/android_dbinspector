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
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import org.slf4j.event.Level

internal class ApiServer(
    private val context: Context,
    private val port: Int = 3700
) : Server {

    internal companion object {
        private const val VERSION = 1
        private const val gracePeriodMillis: Long = 100L
        private const val timeoutMillis: Long = 100L
    }

    private var currentServer: NettyApplicationEngine? = null

    override fun start() {
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
                    databases(VERSION, DatabaseController(context))
                    schema(VERSION, SchemaController(context))
                    content(VERSION, ContentController(context))
                    pragma(VERSION, PragmaController(context))
                }
            }.start(wait = false)

            currentServer = server
        }
    }

    override fun stop() {
        currentServer?.stop(gracePeriodMillis, timeoutMillis)
        currentServer = null
    }
}
