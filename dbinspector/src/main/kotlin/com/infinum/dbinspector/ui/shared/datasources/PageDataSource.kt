package com.infinum.dbinspector.ui.shared.datasources

import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.base.PageParameters
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.ui.shared.base.BaseDataSource

internal abstract class PageDataSource<T : PageParameters>(
    private val useCase: BaseUseCase<T, Page>
) : BaseDataSource<T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cell> {
        val page = useCase(parameters)

        parameters.page = page.nextPage

        return LoadResult.Page(
            data = page.cells,
            prevKey = null,
            nextKey = page.nextPage,
            itemsAfter = page.afterCount
        )
    }
}
