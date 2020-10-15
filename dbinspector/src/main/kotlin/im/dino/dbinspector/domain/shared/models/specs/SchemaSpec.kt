@file:Suppress("LongParameterList")

package im.dino.dbinspector.domain.shared.models.specs

import im.dino.dbinspector.domain.shared.models.Order
import timber.log.Timber

internal class StatementSpec private constructor(
    private val command: String?,
    private val columns: List<String> = listOf(),
    private val table: String?,
    private val type: String?,
    private val query: String?,
    private val order: Order?,
    private val equalsTo: String?,
    private val limit: Int?
) {
    internal open class Builder(
        private var command: String? = null,
        private var columns: List<String> = listOf(),
        private var table: String? = null,
        private var type: String? = null,
        private var query: String? = null,
        private var order: Order? = null,
        private var equalsTo: String? = null,
        private var limit: Int? = null
    ) {
        fun command(command: String) = apply { this.command = command }
        fun columns(columns: List<String>) = apply { this.columns = columns }
        fun table(table: String) = apply { this.table = table }
        fun type(type: String) = apply { this.type = type }
        fun query(query: String?) = apply { this.query = query }
        fun order(order: Order) = apply { this.order = order }
        fun equalsTo(equalsTo: String) = apply { this.equalsTo = equalsTo }
        fun limit(limit: Int) = apply { this.limit = limit }
        fun build() = StatementSpec(
            command = command,
            columns = columns,
            table = table,
            type = type,
            query = query,
            order = order,
            equalsTo = equalsTo,
            limit = limit
        ).build()
    }

    private fun build(): String =
        StringBuilder()
            .append(buildCommand())
            .append(buildClause())
            .append(buildOrderBy())
            .append(buildLimit())
            .toString()
            .also { Timber.i(it) }

    private fun buildCommand() =
        StringBuilder()
            .append(command)
            .append(buildColumns())
            .append(buildFrom())
            .append(" ")
            .append("\"")
            .append(table)
            .append("\"")
            .toString()

    private fun buildColumns() =
        StringBuilder()
            .append(if (columns.isEmpty()) "" else " ")
            .append(columns.joinToString())
            .toString()

    private fun buildFrom() =
        StringBuilder()
            .append(if (columns.isEmpty()) "" else " ")
            .append(if (columns.isEmpty()) "" else "FROM")
            .toString()

    private fun buildClause() =
        type?.let {
            StringBuilder()
                .append(" ")
                .append("WHERE")
                .append(" ")
                .append("type")
                .append(" ")
                .append("=")
                .append(" ")
                .append("'")
                .append(it)
                .append("'")
                .append(buildLike())
                .append(buildEqualsTo())
                .toString()
        }.orEmpty()

    private fun buildLike() =
        query?.let {
            StringBuilder()
                .append(" ")
                .append("AND")
                .append(" ")
                .append(columns.joinToString(" OR "))
                .append(" ")
                .append("LIKE")
                .append(" ")
                .append("\"%%")
                .append(it)
                .append("%%\"")
                .toString()
        }.orEmpty()

    private fun buildEqualsTo() =
        equalsTo?.let {
            StringBuilder()
                .append(" ")
                .append("AND")
                .append(" ")
                .append(columns.first())
                .append(" ")
                .append("=")
                .append(" ")
                .append("\"")
                .append(it)
                .append("\"")
                .toString()
        }.orEmpty()

    private fun buildOrderBy() =
        order?.let {
            StringBuilder()
                .append(" ")
                .append("ORDER BY")
                .append(" ")
                .append(columns.joinToString())
                .append(" ")
                .append(it.rawValue)
                .toString()
        }.orEmpty()

    private fun buildLimit() =
        limit?.let {
            StringBuilder()
                .append(" ")
                .append("LIMIT")
                .append(" ")
                .append(it)
                .toString()
        }.orEmpty()
}

@DslMarker
internal annotation class StatementSpecDsl

@StatementSpecDsl
internal class StatementSpecBuilder : StatementSpec.Builder()

internal inline fun statementSpec(builder: StatementSpecBuilder.() -> Unit): String {
    val specBuilder = StatementSpecBuilder()
    specBuilder.builder()
    return specBuilder.build()
}
