package com.infinum.dbinspector.ui.pragma.foreignkeys

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Query
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.ui.shared.base.BaseDataSource

internal class ForeignKeysDataSource(
    databasePath: String,
    statement: String,
    private val getPragma: UseCases.GetForeignKeys
) : BaseDataSource<PragmaParameters.ForeignKeys>() {

    override var parameters = PragmaParameters.ForeignKeys(
        databasePath = databasePath,
        statement = statement
    )

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cell> {
        val page = getPragma(parameters)
        parameters = parameters.copy(page = page.nextPage)
        return LoadResult.Page(
            data = page.fields,
            prevKey = null,
            nextKey = page.nextPage,
            itemsAfter = page.afterCount
        )
    }
}
