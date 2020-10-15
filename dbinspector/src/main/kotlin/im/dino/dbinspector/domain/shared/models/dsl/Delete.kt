package im.dino.dbinspector.domain.shared.models.dsl

import im.dino.dbinspector.domain.shared.models.dsl.conditions.And
import im.dino.dbinspector.domain.shared.models.dsl.shared.Condition
import timber.log.Timber

class Delete {

    companion object {
        val pattern = "\\s+".toRegex()
    }

    private lateinit var table: String
    private var condition: Condition? = null

    fun from(table: String) {
        this.table = "\"$table\""
    }

    fun where(initializer: Condition.() -> Unit) {
        condition = And().apply(initializer)
    }

    fun build(): String {
        if (!::table.isInitialized) {
            throw IllegalStateException("Failed to build - target table is undefined")
        }
        return toString().also {
            Timber.i(it)
        }
    }

    override fun toString(): String {
        val conditions = condition?.let { "WHERE $it" }.orEmpty()

        return "DELETE FROM $table $conditions".replace(pattern, " ")
    }
}
