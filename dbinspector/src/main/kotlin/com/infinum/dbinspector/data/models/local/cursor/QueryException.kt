package com.infinum.dbinspector.data.models.local.cursor

internal class QueryException : IllegalStateException() {

    override val message: String?
        get() = "Cannot perform a query using a closed database connection."
}
