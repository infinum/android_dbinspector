package com.infinum.dbinspector.data.source.memory.pagination

import com.infinum.dbinspector.extensions.orZero
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

internal class CursorPaginator : Paginator {

    private var pageCount: Int = 0

    override fun setPageCount(rowCount: Int, pageSize: Int) {
        pageCount = ceil((rowCount.toDouble()) / pageSize.toDouble()).roundToInt()
    }

    override fun nextPage(currentPage: Int?): Int? =
        if (pageCount == 0) {
            null
        } else {
            if (currentPage == pageCount) {
                null
            } else {
                currentPage?.inc()
            }
        }

    override fun boundary(page: Int?, pageSize: Int, rowCount: Int): Paginator.Boundary {
        val startRow = page?.minus(1)?.times(pageSize).orZero()
        val endRow = min(startRow + pageSize, rowCount)
        return Paginator.Boundary(
            startRow = startRow,
            endRow = endRow
        )
    }

    override fun count(startRow: Int, endRow: Int, rowCount: Int, columnCount: Int): Paginator.Count {
        val beforeCount = startRow * columnCount
        val afterCount = (rowCount - endRow) * columnCount
        return Paginator.Count(
            beforeCount = beforeCount,
            afterCount = afterCount
        )
    }
}
