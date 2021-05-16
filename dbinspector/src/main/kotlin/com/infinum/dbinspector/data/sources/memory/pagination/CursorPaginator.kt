package com.infinum.dbinspector.data.sources.memory.pagination

import androidx.annotation.VisibleForTesting
import com.infinum.dbinspector.extensions.orZero
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

internal class CursorPaginator : Paginator {

    @VisibleForTesting
    internal var pageCount: Int = 0

    override fun setPageCount(rowCount: Int, pageSize: Int) {
        pageCount = ceil((rowCount.coerceAtLeast(0).toDouble()) / pageSize.coerceAtLeast(1).toDouble())
            .roundToInt()
    }

    override fun nextPage(currentPage: Int?): Int? =
        if (pageCount == 0) {
            null
        } else {
            val coercedCurrentPage = currentPage?.coerceAtLeast(0)
            if (coercedCurrentPage == pageCount) {
                null
            } else {
                coercedCurrentPage?.inc()
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
