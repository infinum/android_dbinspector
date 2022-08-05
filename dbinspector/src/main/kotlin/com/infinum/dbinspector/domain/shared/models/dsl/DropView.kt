package com.infinum.dbinspector.domain.shared.models.dsl

internal class DropView {

    companion object {
        val pattern = "\\s+".toRegex()
    }

    private lateinit var view: String

    fun name(view: String) {
        this.view = "\"$view\""
    }

    fun build(): String {
        if (!::view.isInitialized) {
            error("Failed to build - target view is undefined")
        }
        return toString()
    }

    override fun toString(): String {
        return "DROP VIEW $view".replace(pattern, " ")
    }
}
