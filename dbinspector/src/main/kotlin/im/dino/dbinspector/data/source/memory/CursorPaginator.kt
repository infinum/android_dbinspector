package im.dino.dbinspector.data.source.memory

import im.dino.dbinspector.extensions.orZero
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

internal class CursorPaginator : Paginator {

    private var pageCount: Int = 0

    private var currentPage: Int = 0

    override fun setPageCount(rowCount: Int, columnCount: Int, pageSize: Int) {
        pageCount = ceil((rowCount.toDouble() * columnCount) / pageSize).roundToInt()
        initialiseCurrentPage()
    }

    override fun nextPage(): Int? =
        takeUnless { currentPage == pageCount }?.incrementCurrentPage()

    override fun boundary(page: Int?, pageSize: Int, count: Int): Paginator.Boundary {
        val startRow = page?.takeIf { it > 1 }?.times(pageSize).orZero()
        val endRow = min(startRow + pageSize, count)
        return Paginator.Boundary(
            startRow = startRow,
            endRow = endRow
        )
    }

    private fun initialiseCurrentPage() {
        if (pageCount > 0) {
            currentPage = currentPage.inc()
        }
    }

    private fun incrementCurrentPage(): Int {
        currentPage = currentPage.inc()
        return currentPage
    }
}