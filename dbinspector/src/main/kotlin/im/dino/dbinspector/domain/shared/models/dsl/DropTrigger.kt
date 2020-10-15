package im.dino.dbinspector.domain.shared.models.dsl

import timber.log.Timber

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
        return toString().also {
            Timber.i(it)
        }
    }

    override fun toString(): String {
        return "DROP TRIGGER $trigger".replace(pattern, " ")
    }
}
