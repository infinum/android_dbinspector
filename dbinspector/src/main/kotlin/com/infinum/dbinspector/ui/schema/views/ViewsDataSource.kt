package com.infinum.dbinspector.ui.schema.views

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Query
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.ui.schema.shared.SchemaDataSource

internal class ViewsDataSource(
    databasePath: String,
    statement: String,
    private val getSchema: UseCases.GetViews
) : SchemaDataSource<ContentParameters>() {

    override var parameters: ContentParameters = ContentParameters(
        databasePath = databasePath,
        statement = statement
    )

    override var argument: String?
        get() {
            return ""
        }
        set(value) {
            parameters = parameters.copy(
                statement = Statements.Schema.views(query = value),
                page = 1
            )
            invalidate()
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cell> {
        val page = getSchema(parameters)
        parameters = parameters.copy(page = page.nextPage)
        return LoadResult.Page(
            data = page.fields,
            prevKey = null,
            nextKey = page.nextPage,
            itemsAfter = page.afterCount
        )
    }
}
