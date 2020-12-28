package com.infinum.dbinspector.ui.content.table

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Query
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.ui.shared.base.BaseDataSource
import timber.log.Timber

internal class TableDataSource(
    databasePath: String,
    statement: String,
    private val getSchema: UseCases.GetTable
) : BaseDataSource<ContentParameters>() {

    override var parameters: ContentParameters = ContentParameters(
        databasePath = databasePath,
        statement = statement
    )

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cell> {
        val page = getSchema(parameters)

        Timber.i("load params.key: ${params.key} - next page: ${page.nextPage} - itemsAfter: ${page.afterCount}")

        parameters = parameters.copy(page = page.nextPage)
        return LoadResult.Page(
            data = page.fields,
            prevKey = null,
            nextKey = page.nextPage,
            itemsAfter = page.afterCount
        )
    }
}
