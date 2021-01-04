package com.infinum.dbinspector.domain.shared.models.dsl

internal class Changes {

    fun build(): String {
        return toString()
    }

    override fun toString(): String {
        return "SELECT changes()"
    }
}
