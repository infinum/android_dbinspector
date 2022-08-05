package com.infinum.dbinspector.domain.shared.models.dsl.conditions

import com.infinum.dbinspector.domain.shared.models.dsl.shared.Condition

internal class Like(
    private val column: String,
    private val value: Any
) : Condition() {

    init {
        val isNotNumber = value !is Number
        val isNotString = value !is String
        if (isNotNumber && isNotString) {
            throw IllegalArgumentException("Only number and string values can be used in the 'LIKE' condition")
        }
    }

    override fun addCondition(condition: Condition): Unit =
        error("Can't add a nested condition to 'like'")

    override fun toString(): String =
        "$column LIKE \"%%$value%%\""
}
