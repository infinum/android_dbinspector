package im.dino.dbinspector.ui.content.shared

import androidx.paging.PagingSource
import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.pragma.schema.ForeignKeysOperation
import im.dino.dbinspector.domain.shared.AbstractSchemaOperation

internal abstract class ContentDataSource(
    private val path: String
) : PagingSource<Int, String>() {

    abstract fun operation(): Lazy<AbstractSchemaOperation<List<Row>>>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val response = operation().value(path, params.key)
            .map { it.fields.toList() }
            .flatten()
        return LoadResult.Page(
            data = response,
            prevKey = null,
            nextKey = operation().value.nextPage()
        )
    }
}