package com.infinum.dbinspector.data.models.local

internal class CursorException : IllegalStateException() {

    override val message: String?
        get() = "Cannot obtain a raw query cursor."
}
