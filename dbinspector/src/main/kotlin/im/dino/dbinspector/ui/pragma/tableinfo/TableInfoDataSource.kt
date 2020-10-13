package im.dino.dbinspector.ui.pragma.tableinfo

import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Query
import im.dino.dbinspector.ui.shared.base.BaseDataSource
import im.dino.dbinspector.ui.shared.base.BaseViewModel

internal class TableInfoDataSource(
    databasePath: String,
    statement: String,
    private val getPragma: UseCases.GetTablePragma
) : BaseDataSource() {

    override var query: Query = Query(
        databasePath = databasePath,
        statement = statement,
        pageSize = BaseViewModel.PAGE_SIZE
    )

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val page = getPragma(query)
        query = query.copy(page = page.nextPage)
        return LoadResult.Page(
            data = page.fields,
            prevKey = null,
            nextKey = page.nextPage
        )
    }
}
