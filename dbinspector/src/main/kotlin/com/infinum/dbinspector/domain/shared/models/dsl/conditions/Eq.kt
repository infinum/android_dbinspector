package com.infinum.dbinspector.domain.shared.models.dsl.conditions

import com.infinum.dbinspector.domain.shared.models.dsl.shared.Condition

internal class Eq(
    private val column: String,
    private val value: Any?
) : Condition() {

    init {
        require(value == null || value is Number || value is String) {
            "Only null, number and string values can be used in the 'WHERE' clause"
        }
    }

    override fun addCondition(condition: Condition) {
        error("Can't add a nested condition to 'eq'")
    }

    override fun toString(): String =
        when (value) {
            null -> "$column IS NULL"
            is String -> "$column = '$value'"
            else -> "$column = $value"
        }
}
