package com.infinum.dbinspector.server.routes

import com.infinum.dbinspector.data.models.remote.DatabaseResponse
import com.infinum.dbinspector.server.controllers.DatabaseController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route

internal fun Route.databases(controller: DatabaseController): Route =
    route("/databases") {
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
                call.respond(it)
            } ?: call.respondText(
                "No database with id $id", status = HttpStatusCode.NotFound
            )
        }
        post {
            val id = call.parameters["id"] ?: return@post call.respondText(
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
