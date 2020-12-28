package com.infinum.dbinspector.ui.pragma.indexes

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.ui.shared.base.BaseDataSource

internal class IndexDataSource(
    databasePath: String,
    statement: String,
    private val getPragma: UseCases.GetIndexes
) : BaseDataSource<PragmaParameters.Indexes>() {

    override var parameters: PragmaParameters.Indexes = PragmaParameters.Indexes(
        databasePath = databasePath,
        statement = statement
    )

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cell> {
        val page = getPragma(parameters)
        parameters = parameters.copy(page = page.nextPage)
        return LoadResult.Page(
            data = page.cells,
            prevKey = null,
            nextKey = page.nextPage,
            itemsAfter = page.afterCount
        )
    }
}
