package im.dino.dbinspector.ui.schema.views

import androidx.paging.PagingSource
import im.dino.dbinspector.domain.schema.view.AllViewsOperation

internal class ViewsDataSource(
    private val path: String,
    pageSize: Int,
    args: String?,
    private val empty: suspend (value: Boolean) -> Unit
) : PagingSource<Int, String>() {

    private val source = AllViewsOperation(pageSize, args)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val result = source(path, params.key)
            .map { it.fields.first() }

        empty(params.key == null && result.isEmpty())

        return LoadResult.Page(
            data = result,
            prevKey = null,
            nextKey = source.nextPage()
        )
    }
}