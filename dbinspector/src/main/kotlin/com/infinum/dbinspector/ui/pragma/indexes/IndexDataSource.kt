package com.infinum.dbinspector.ui.pragma.indexes

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Query
import com.infinum.dbinspector.ui.shared.base.BaseDataSource

internal class IndexDataSource(
    databasePath: String,
    statement: String,
    private val getPragma: UseCases.GetIndexes
) : BaseDataSource() {

    override var query: Query = Query(
        databasePath = databasePath,
        statement = statement
    )

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val page = getPragma(query)
        query = query.copy(page = page.nextPage)
        return LoadResult.Page(
            data = page.fields,
            prevKey = null,
            nextKey = page.nextPage,
            itemsAfter = page.afterCount
        )
    }
}
