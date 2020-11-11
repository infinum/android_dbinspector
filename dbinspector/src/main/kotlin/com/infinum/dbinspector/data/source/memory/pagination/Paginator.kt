package com.infinum.dbinspector.data.source.memory.pagination

internal interface Paginator {

    fun setPageCount(rowCount: Int, pageSize: Int)

    fun nextPage(currentPage: Int?): Int?

    fun boundary(page: Int?, pageSize: Int, rowCount: Int): Boundary

    fun count(startRow: Int, endRow: Int, rowCount: Int, columnCount: Int): Count

    data class Boundary(
        val startRow: Int,
        val endRow: Int
    )

    data class Count(
        val beforeCount: Int,
        val afterCount: Int
    )
}
