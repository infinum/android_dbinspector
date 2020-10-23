package com.infinum.dbinspector.domain.shared.models.dsl.conditions

import com.infinum.dbinspector.domain.shared.models.dsl.shared.Condition

class Like(
    private val column: String,
    private val value: Any?
) : Condition() {

    init {
        if (value == null && value !is Number && value !is String) {
            throw IllegalArgumentException("Only number and string values can be used in the 'LIKE' condition")
        }
    }

    override fun addCondition(condition: Condition) {
        throw IllegalStateException("Can't add a nested condition to 'like'")
    }

    override fun toString(): String =
        "$column LIKE \"%%$value%%\""
}
