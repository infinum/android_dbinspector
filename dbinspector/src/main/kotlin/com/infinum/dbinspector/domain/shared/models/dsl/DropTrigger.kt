package com.infinum.dbinspector.domain.shared.models.dsl

class DropTrigger {

    companion object {
        val pattern = "\\s+".toRegex()
    }

    private lateinit var trigger: String

    fun name(trigger: String) {
        this.trigger = "\"$trigger\""
    }

    fun build(): String {
        if (!::trigger.isInitialized) {
            throw IllegalStateException("Failed to build - target view is undefined")
        }
        return toString()
    }

    override fun toString(): String {
        return "DROP TRIGGER $trigger".replace(pattern, " ")
    }
}
