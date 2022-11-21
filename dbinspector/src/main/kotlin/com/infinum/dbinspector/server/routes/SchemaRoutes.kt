package com.infinum.dbinspector.server.routes

import com.infinum.dbinspector.server.controllers.SchemaController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

internal fun Route.schema(version: Int, controller: SchemaController): Route =
    route("/api/v$version/databases/{database_id?}") {
        get("tables") {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val query = call.parameters["query"]

            controller.getTablesById(databaseId, query)?.let {
                call.respond(it)
            } ?: call.respondText(
                "No tables from database by id $databaseId", status = HttpStatusCode.NotFound
            )
        }
        get("views") {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val query = call.parameters["query"]

            controller.getViewsById(databaseId, query)?.let {
                call.respond(it)
            } ?: call.respondText(
                "No views from database by id $databaseId", status = HttpStatusCode.NotFound
            )
        }
        get("triggers") {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val query = call.parameters["query"]

            controller.getTriggersById(databaseId, query)?.let {
                call.respond(it)
            } ?: call.respondText(
                "No triggers from database by id $databaseId", status = HttpStatusCode.NotFound
            )
        }
    }
