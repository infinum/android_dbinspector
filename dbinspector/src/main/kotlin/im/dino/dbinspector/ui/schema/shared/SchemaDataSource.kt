package im.dino.dbinspector.ui.schema.shared

import androidx.paging.PagingSource
import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.shared.AbstractDatabaseOperation

internal abstract class SchemaDataSource(
    private val path: String,
    private val empty: suspend (value: Boolean) -> Unit
) : PagingSource<Int, String>() {

    abstract val source: Lazy<AbstractDatabaseOperation<List<Row>>>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val result = source.value(path, params.key)
            .map { it.fields.first() }

        empty(params.key == null && result.isEmpty())

        return LoadResult.Page(
            data = result,
            prevKey = null,
            nextKey = source.value.nextPage()
        )
    }
}