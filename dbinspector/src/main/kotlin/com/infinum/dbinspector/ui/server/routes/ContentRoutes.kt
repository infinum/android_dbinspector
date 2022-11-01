package com.infinum.dbinspector.ui.server.routes

import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.ui.server.controllers.ContentController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.route

internal fun Route.content(version: Int, controller: ContentController): Route = apply {
    route("/api/v$version/databases/{database_id?}/tables/{table_id?}") {
        get {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val tableId = call.parameters["table_id"] ?: return@get call.respondText(
                "Missing table id", status = HttpStatusCode.BadRequest
            )
            val orderBy = call.parameters["orderBy"]
            val sort = call.parameters["sort"] ?: "ASC"

            controller.getTable(
                databaseId = databaseId,
                tableId = tableId,
                orderBy = orderBy,
                sort = Sort(sort) ?: Sort.ASCENDING
            )?.let {
                call.respond(it)
            } ?: call.respondText(
                "No table content from database by id $databaseId and table by id $tableId",
                status = HttpStatusCode.NotFound
            )
        }
        delete {
            val databaseId = call.parameters["database_id"] ?: return@delete call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val tableId = call.parameters["table_id"] ?: return@delete call.respondText(
                "Missing table id", status = HttpStatusCode.BadRequest
            )

            controller.dropTable(
                databaseId = databaseId,
                tableId = tableId
            )?.let {
                call.respond(
                    status = HttpStatusCode.Accepted,
                    message = it
                )
            } ?: call.respondText(
                "No table content deleted from database by id $databaseId and table by id $tableId",
                status = HttpStatusCode.NotFound
            )
        }
    }
    route("/api/v$version/databases/{database_id?}/views/{view_id?}") {
        get {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val viewId = call.parameters["view_id"] ?: return@get call.respondText(
                "Missing view id", status = HttpStatusCode.BadRequest
            )
            val orderBy = call.parameters["orderBy"]
            val sort = call.parameters["sort"] ?: "ASC"

            controller.getView(
                databaseId = databaseId,
                viewId = viewId,
                orderBy = orderBy,
                sort = Sort(sort) ?: Sort.ASCENDING
            )?.let {
                call.respond(it)
            } ?: call.respondText(
                "No view content from database by id $databaseId and view by id $viewId",
                status = HttpStatusCode.NotFound
            )
        }
        delete {
            val databaseId = call.parameters["database_id"] ?: return@delete call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val viewId = call.parameters["view_id"] ?: return@delete call.respondText(
                "Missing view id", status = HttpStatusCode.BadRequest
            )

            controller.dropView(
                databaseId = databaseId,
                viewId = viewId
            )?.let {
                call.respond(
                    status = HttpStatusCode.Accepted,
                    message = it
                )
            } ?: call.respondText(
                "No view dropped from database by id $databaseId and view by id $viewId",
                status = HttpStatusCode.NotFound
            )
        }
    }
    route("/api/v$version/databases/{database_id?}/triggers/{trigger_id?}") {
        get {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val triggerId = call.parameters["trigger_id"] ?: return@get call.respondText(
                "Missing trigger id", status = HttpStatusCode.BadRequest
            )

            controller.getTrigger(
                databaseId = databaseId,
                triggerId = triggerId
            )?.let {
                call.respond(it)
            } ?: call.respondText(
                "No trigger content from database by id $databaseId and trigger by id $triggerId",
                status = HttpStatusCode.NotFound
            )
        }
        delete {
            val databaseId = call.parameters["database_id"] ?: return@delete call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val triggerId = call.parameters["trigger_id"] ?: return@delete call.respondText(
                "Missing trigger id", status = HttpStatusCode.BadRequest
            )

            controller.dropTrigger(
                databaseId = databaseId,
                triggerId = triggerId
            )?.let {
                call.respond(
                    status = HttpStatusCode.Accepted,
                    message = it
                )
            } ?: call.respondText(
                "No trigger dropped from database by id $databaseId and view by id $triggerId",
                status = HttpStatusCode.NotFound
            )
        }
    }
}
