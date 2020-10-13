package im.dino.dbinspector.ui.content.table

import im.dino.dbinspector.BuildConfig
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Query
import im.dino.dbinspector.ui.shared.base.BaseDataSource
import im.dino.dbinspector.ui.shared.base.BaseViewModel

internal class TableDataSource(
    databasePath: String,
    statement: String,
    private val getSchema: UseCases.GetTable
) : BaseDataSource() {

    override var query: Query = Query(
        databasePath = databasePath,
        statement = statement
    )

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val page = getSchema(query)
        if (BuildConfig.DEBUG) {
            println("TableDataSource - load params.key: ${params.key} - next page: ${page.nextPage}")
        }
        query = query.copy(page = page.nextPage)
        return LoadResult.Page(
            data = page.fields,
            prevKey = null,
            nextKey = page.nextPage
        )
    }
}