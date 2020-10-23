package com.infinum.dbinspector.data.source.memory.pagination

import com.infinum.dbinspector.extensions.orZero
import timber.log.Timber
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

internal class CursorPaginator : Paginator {

    private var pageCount: Int = 0

    override fun setPageCount(rowCount: Int, pageSize: Int) {
        pageCount = ceil((rowCount.toDouble()) / pageSize.toDouble()).roundToInt()
        Timber.i(
            "setPageCount -> rowCount/pageSize: ${rowCount.toDouble()}/${pageSize.toDouble()} | pageCount: $pageCount"
        )
    }

    override fun nextPage(currentPage: Int?): Int? =
        if (pageCount == 0) {
            Timber.i("nextPage -> no pages | return null")
            null
        } else {
            if (currentPage == pageCount) {
                Timber.i("nextPage -> end of pages | return null")
                null
            } else {
                val nextPage = currentPage?.inc()
                Timber.i("nextPage -> next page | return $nextPage")
                nextPage
            }
        }

    override fun boundary(page: Int?, pageSize: Int, rowCount: Int): Paginator.Boundary {
        val startRow = page?.minus(1)?.times(pageSize).orZero()
        val endRow = min(startRow + pageSize, rowCount)
        Timber.i("boundary -> page/pageCount: $page/$pageCount | startRow - endRow: $startRow - $endRow")
        return Paginator.Boundary(
            startRow = startRow,
            endRow = endRow
        )
    }

    override fun count(startRow: Int, endRow: Int, rowCount: Int, columnCount: Int): Paginator.Count {
        val beforeCount = startRow * columnCount
        val afterCount = (rowCount - endRow) * columnCount
        Timber.i("count -> startRow->endRow: $startRow->$endRow | beforeCount - afterCount: $beforeCount - $afterCount")
        return Paginator.Count(
            beforeCount = beforeCount,
            afterCount = afterCount
        )
    }
}
