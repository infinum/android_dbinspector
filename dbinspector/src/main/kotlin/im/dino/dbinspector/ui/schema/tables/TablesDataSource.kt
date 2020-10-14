package im.dino.dbinspector.ui.schema.tables

import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Query
import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.schema.shared.SchemaDataSource

internal class TablesDataSource(
    databasePath: String,
    statement: String,
    private val getSchema: UseCases.GetTables
) : SchemaDataSource() {

    override var query: Query = Query(
        databasePath = databasePath,
        statement = statement
    )

    override var argument: String?
        get() {
            return ""
        }
        set(value) {
            query = query.copy(
                statement = Statements.Schema.tables(query = value),
                page = 1
            )
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val page = getSchema(query)
        query = query.copy(page = page.nextPage)
        return LoadResult.Page(
            data = page.fields,
            prevKey = null,
            nextKey = page.nextPage
        )
    }
}
