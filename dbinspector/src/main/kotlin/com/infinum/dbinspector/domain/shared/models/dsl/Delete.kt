package com.infinum.dbinspector.domain.shared.models.dsl

import com.infinum.dbinspector.domain.shared.models.dsl.conditions.And
import com.infinum.dbinspector.domain.shared.models.dsl.shared.Condition

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
        return toString()
    }

    override fun toString(): String {
        val conditions = condition?.let { "WHERE $it" }.orEmpty()

        return "DELETE FROM $table $conditions".replace(pattern, " ")
    }
}
