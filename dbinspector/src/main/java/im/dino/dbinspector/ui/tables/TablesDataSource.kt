package im.dino.dbinspector.ui.tables

import androidx.paging.PagingSource
import im.dino.dbinspector.domain.database.AllTablesOperation
import im.dino.dbinspector.domain.table.models.Table

class TablesDataSource(
    private val path: String,
    pageSize: Int
) : PagingSource<Int, Table>() {

    private val source = AllTablesOperation(pageSize)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Table> {
        val result = source(path, params.key).map {
            Table(name = it.fields.first())
        }
        return LoadResult.Page(
            data = result,
            prevKey = null,
            nextKey = source.nextPage()
        )
    }
}