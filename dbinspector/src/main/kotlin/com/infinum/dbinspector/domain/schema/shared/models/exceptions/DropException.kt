package com.infinum.dbinspector.domain.schema.shared.models.exceptions

internal class DropException : IllegalStateException() {

    override val message: String?
        get() = "Cannot perform a drop on selected schema."
}
