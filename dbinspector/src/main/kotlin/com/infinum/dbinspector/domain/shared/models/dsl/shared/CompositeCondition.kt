package com.infinum.dbinspector.domain.shared.models.dsl.shared

open class CompositeCondition(private val sqlOperator: String) : Condition() {

    private val conditions = mutableListOf<Condition>()

    override fun addCondition(condition: Condition) {
        conditions += condition
    }

    override fun toString(): String =
        if (conditions.size == 1) {
            conditions.first().toString()
        } else {
            conditions.joinToString(
                prefix = "(",
                postfix = ")",
                separator = " $sqlOperator "
            ) {
                "$it"
            }
        }
}
