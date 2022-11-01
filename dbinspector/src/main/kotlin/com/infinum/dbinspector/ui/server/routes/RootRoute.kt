package com.infinum.dbinspector.ui.server.routes

import io.ktor.server.http.content.files
import io.ktor.server.http.content.static
import io.ktor.server.routing.Route
import java.io.File

internal fun Route.root(index: File): Route =
    static("/") {
        files(index)
    }