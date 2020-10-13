package im.dino.dbinspector.data.source.memory

import im.dino.dbinspector.BuildConfig
import im.dino.dbinspector.extensions.orZero
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

internal class CursorPaginator : Paginator {

    private var pageCount: Int = 0

    override fun setPageCount(rowCount: Int, pageSize: Int) {
        pageCount = ceil((rowCount.toDouble()) / pageSize.toDouble()).roundToInt()
        if (BuildConfig.DEBUG) {
            println("CursorPaginator | setPageCount -> rowCount / pageSize: ${rowCount.toDouble()} * ${pageSize.toDouble()} | pageCount: $pageCount")
        }
    }

    override fun nextPage(currentPage: Int?): Int? =
        if (pageCount == 0) {
            if (BuildConfig.DEBUG) {
                println("CursorPaginator | nextPage -> no pages | return null")
            }
            null
        } else {
            if (currentPage == pageCount) {
                if (BuildConfig.DEBUG) {
                    println("CursorPaginator | nextPage -> end of pages | return null")
                }
                null
            } else {
                val nextPage = currentPage?.inc()
                if (BuildConfig.DEBUG) {
                    println("CursorPaginator | nextPage -> next page | return $nextPage")
                }
                nextPage
            }
        }

    override fun boundary(page: Int?, pageSize: Int, count: Int): Paginator.Boundary {
        val startRow = page?.minus(1)?.times(pageSize).orZero()
        val endRow = min(startRow + pageSize, count)
        if (BuildConfig.DEBUG) {
            println("CursorPaginator | boundary -> page/pageCount: $page/$pageCount | startRow - endRow: $startRow - $endRow")
        }
        return Paginator.Boundary(
            startRow = startRow,
            endRow = endRow
        )
    }
}