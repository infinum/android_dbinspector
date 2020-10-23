package com.infinum.dbinspector.ui.pragma.tableinfo

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Query
import com.infinum.dbinspector.ui.shared.base.BaseDataSource

internal class TableInfoDataSource(
    databasePath: String,
    statement: String,
    private val getPragma: UseCases.GetTablePragma
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
