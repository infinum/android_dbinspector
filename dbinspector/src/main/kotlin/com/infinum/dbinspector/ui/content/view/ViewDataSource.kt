package com.infinum.dbinspector.ui.content.view

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Query
import com.infinum.dbinspector.ui.shared.base.BaseDataSource
import timber.log.Timber

internal class ViewDataSource(
    databasePath: String,
    statement: String,
    private val getSchema: UseCases.GetView
) : BaseDataSource() {

    override var query: Query = Query(
        databasePath = databasePath,
        statement = statement
    )

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val page = getSchema(query)

        Timber.i("load params.key: ${params.key} - next page: ${page.nextPage} - itemsAfter: ${page.afterCount}")

        query = query.copy(page = page.nextPage)
        return LoadResult.Page(
            data = page.fields,
            prevKey = null,
            nextKey = page.nextPage,
            itemsAfter = page.afterCount
        )
    }
}
