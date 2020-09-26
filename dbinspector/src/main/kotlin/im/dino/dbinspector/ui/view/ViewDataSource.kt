package im.dino.dbinspector.ui.view

import androidx.paging.PagingSource
import im.dino.dbinspector.domain.schema.view.ViewContentOperation

internal class ViewDataSource(
    private val path: String,
    name: String,
    pageSize: Int
) : PagingSource<Int, String>() {

    private val operation = ViewContentOperation(name, pageSize)

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