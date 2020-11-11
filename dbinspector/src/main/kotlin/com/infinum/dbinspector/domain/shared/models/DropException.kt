package com.infinum.dbinspector.domain.shared.models

class DropException : IllegalStateException() {

    override val message: String?
        get() = "Cannot perform a drop on selected schema."
}
