package im.dino.dbinspector.ui.pragma.foreignkeys

import androidx.paging.PagingSource
import im.dino.dbinspector.domain.pragma.ForeignKeysOperation

internal class ForeignKeyDataSource(
    private val path: String,
    name: String,
    pageSize: Int
) : PagingSource<Int, String>() {

    private val operation = ForeignKeysOperation(name, pageSize)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val response = operation(path, params.key)
            .map { it.fields.toList() }
            .flatten()
        return LoadResult.Page(
            data = response,
            prevKey = null,
            nextKey = operation.nextPage()
        )
    }
}