package com.infinum.dbinspector.ui.server.routes

import com.infinum.dbinspector.data.models.remote.DatabaseResponse
import com.infinum.dbinspector.ui.server.controllers.DatabaseController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.io.File

@Suppress("ComplexMethod")
internal fun Route.databases(version: Int, controller: DatabaseController): Route =
    route("/api/v$version/databases") {
        get {
            val query: String? = call.parameters["query"]
            val response: List<DatabaseResponse> = controller.getAll(query)
            call.respond(response)
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id", status = HttpStatusCode.BadRequest
            )

            controller.getById(id)?.let {
                val file = File(it.path)
                if (file.exists()) {
                    call.response.header("Content-Disposition", "attachment; filename=\"${file.name}\"")
                    call.respondFile(file)
                } else {
                    call.respondText(
                        "No database file found", status = HttpStatusCode.NotFound
                    )
                }
            } ?: call.respondText(
                "No database with id $id", status = HttpStatusCode.NotFound
            )
        }
        post {
            val id = call.receiveParameters()["id"] ?: return@post call.respondText(
                "Missing id", status = HttpStatusCode.BadRequest
            )

            controller.copy(id)?.let { response ->
                call.respond(
                    status = HttpStatusCode.Created, response
                )
            } ?: call.respondText("Not Found", status = HttpStatusCode.NotFound)
        }
        patch("{id?}") {
            val id = call.parameters["id"] ?: return@patch call.respondText(
                "Missing id", status = HttpStatusCode.BadRequest
            )
            val newName = call.receiveParameters()["name"] ?: return@patch call.respondText(
                "Missing new name", status = HttpStatusCode.BadRequest
            )

            controller.rename(id, newName)?.let {
                call.respond(
                    status = HttpStatusCode.Accepted, it
                )
            } ?: call.respondText("Not Found", status = HttpStatusCode.NotFound)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing id", status = HttpStatusCode.BadRequest
            )

            controller.remove(id)?.let {
                call.respondText(
                    "", status = HttpStatusCode.Accepted
                )
            } ?: call.respondText("Not Found", status = HttpStatusCode.NotFound)
        }
    }
