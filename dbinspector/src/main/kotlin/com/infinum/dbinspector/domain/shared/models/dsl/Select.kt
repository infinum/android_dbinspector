package com.infinum.dbinspector.domain.shared.models.dsl

import com.infinum.dbinspector.domain.shared.models.Direction
import com.infinum.dbinspector.domain.shared.models.dsl.conditions.And
import com.infinum.dbinspector.domain.shared.models.dsl.shared.Condition

class Select {

    companion object {
        val pattern = "\\s+".toRegex()
    }

    private val columns = mutableListOf<String>()
    private lateinit var table: String
    private var condition: Condition? = null
    private var orderByColumns = listOf<String>()
    private var orderByDirection: Direction = Direction.ASCENDING
    private var limit: Int? = null

    fun columns(vararg columns: String) {
        if (this.columns.isNotEmpty()) {
            throw IllegalStateException("Detected an attempt to re-define columns to fetch.")
        }
        this.columns.addAll(columns)
    }

    fun from(table: String) {
        this.table = "\"$table\""
    }

    fun where(initializer: Condition.() -> Unit) {
        condition = And().apply(initializer)
    }

    fun orderBy(direction: Direction, vararg columns: String?) {
        this.orderByDirection = direction

        if (columns.isEmpty()) {
            throw IllegalArgumentException("At least one column should be defined")
        }
        if (this.orderByColumns.isNotEmpty()) {
            throw IllegalStateException("Detected an attempt to re-define ORDER BY columns.")
        }
        this.orderByColumns = columns.toList().filterNotNull()
    }

    fun limit(limit: Int) {
        this.limit = limit
    }

    fun build(): String {
        if (!::table.isInitialized) {
            throw IllegalStateException("Failed to build - target table is undefined")
        }
        return toString()
    }

    override fun toString(): String {
        val queryColumns = if (columns.isEmpty()) {
            "*"
        } else {
            columns.joinToString(separator = ", ")
        }

        val conditions = condition?.let { "WHERE $it" }.orEmpty()

        val orderBy = if (orderByColumns.isNotEmpty()) {
            "${
                orderByColumns.joinToString(
                    prefix = "ORDER BY ",
                    separator = ", "
                )
            } ${orderByDirection.rawValue}"
        } else {
            ""
        }

        val limit = limit?.let { "LIMIT $it" }.orEmpty()

        return "SELECT $queryColumns FROM $table $conditions $orderBy $limit".replace(pattern, " ")
    }
}
