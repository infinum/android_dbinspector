package im.dino.dbinspector.domain.shared.models.dsl

import timber.log.Timber

class DropView {

    companion object {
        val pattern = "\\s+".toRegex()
    }

    private lateinit var view: String

    fun name(view: String) {
        this.view = "\"$view\""
    }

    fun build(): String {
        if (!::view.isInitialized) {
            throw IllegalStateException("Failed to build - target view is undefined")
        }
        return toString().also {
            Timber.i(it)
        }
    }

    override fun toString(): String {
        return "DROP VIEW $view".replace(pattern, " ")
    }
}
