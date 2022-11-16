package com.infinum.dbinspector.ui.server.routes

import com.infinum.dbinspector.ui.server.controllers.PragmaController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

@Suppress("ComplexMethod", "LongMethod")
internal fun Route.pragma(version: Int, controller: PragmaController): Route = apply {
    route("/api/v$version/databases/{database_id?}/tables/{table_id?}/pragma") {
        get("info") {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val tableId = call.parameters["table_id"] ?: return@get call.respondText(
                "Missing table id", status = HttpStatusCode.BadRequest
            )

            controller.getTableInfo(
                databaseId = databaseId,
                id = tableId
            )?.let {
                call.respond(it)
            } ?: call.respondText(
                "No table info from database by id $databaseId and table by id $tableId",
                status = HttpStatusCode.NotFound
            )
        }
        get("foreign_keys") {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val tableId = call.parameters["table_id"] ?: return@get call.respondText(
                "Missing table id", status = HttpStatusCode.BadRequest
            )

            controller.getTableForeignKeys(
                databaseId = databaseId,
                id = tableId
            )?.let {
                call.respond(it)
            } ?: call.respondText(
                "No foreign keys from database by id $databaseId and table by id $tableId",
                status = HttpStatusCode.NotFound
            )
        }
        get("indexes") {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val tableId = call.parameters["table_id"] ?: return@get call.respondText(
                "Missing table id", status = HttpStatusCode.BadRequest
            )

            controller.getTableIndexes(
                databaseId = databaseId,
                id = tableId
            )?.let {
                call.respond(it)
            } ?: call.respondText(
                "No indexes from database by id $databaseId and table by id $tableId",
                status = HttpStatusCode.NotFound
            )
        }
    }
    route("/api/v$version/databases/{database_id?}/views/{view_id?}/pragma") {
        get("info") {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val viewId = call.parameters["view_id"] ?: return@get call.respondText(
                "Missing view id", status = HttpStatusCode.BadRequest
            )

            controller.getViewInfo(
                databaseId = databaseId,
                id = viewId
            )?.let {
                call.respond(it)
            } ?: call.respondText(
                "No table info from database by id $databaseId and view by id $viewId",
                status = HttpStatusCode.NotFound
            )
        }
        get("foreign_keys") {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val viewId = call.parameters["view_id"] ?: return@get call.respondText(
                "Missing view id", status = HttpStatusCode.BadRequest
            )

            controller.getViewForeignKeys(
                databaseId = databaseId,
                id = viewId
            )?.let {
                call.respond(it)
            } ?: call.respondText(
                "No foreign keys from database by id $databaseId and view by id $viewId",
                status = HttpStatusCode.NotFound
            )
        }
        get("indexes") {
            val databaseId = call.parameters["database_id"] ?: return@get call.respondText(
                "Missing database id", status = HttpStatusCode.BadRequest
            )
            val viewId = call.parameters["view_id"] ?: return@get call.respondText(
                "Missing view id", status = HttpStatusCode.BadRequest
            )

            controller.getViewIndexes(
                databaseId = databaseId,
                id = viewId
            )?.let {
                call.respond(it)
            } ?: call.respondText(
                "No indexes from database by id $databaseId and view by id $viewId",
                status = HttpStatusCode.NotFound
            )
        }
    }
}
