package com.infinum.dbinspector.ui.schema.tables

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.ui.schema.shared.SchemaDataSource

internal class TablesDataSource(
    databasePath: String,
    statement: String,
    private val getSchema: UseCases.GetTables
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
                statement = Statements.Schema.tables(query = value),
                page = 1
            )
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cell> {
        val page = getSchema(parameters)
        parameters = parameters.copy(page = page.nextPage)
        return LoadResult.Page(
            data = page.cells,
            prevKey = null,
            nextKey = page.nextPage,
            itemsAfter = page.afterCount
        )
    }
}
