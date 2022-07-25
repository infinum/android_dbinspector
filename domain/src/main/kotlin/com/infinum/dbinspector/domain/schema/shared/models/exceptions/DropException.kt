package com.infinum.dbinspector.domain.schema.shared.models.exceptions

public class DropException : IllegalStateException() {

    override val message: String
        get() = "Cannot perform a drop on selected schema."
}
