package com.infinum.dbinspector.data.interactors.pragma

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.Interactors

internal class GetTableInfoInteractor(
    private val source: com.infinum.dbinspector.data.Sources.Local.Pragma
) : Interactors.GetTableInfo {

    override suspend fun invoke(input: Query): QueryResult =
        source.getTableInfo(input)
}
