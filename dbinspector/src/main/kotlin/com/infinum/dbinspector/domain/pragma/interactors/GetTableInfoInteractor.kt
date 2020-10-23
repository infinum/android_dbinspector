package com.infinum.dbinspector.domain.pragma.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.QueryResult
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.shared.models.Query

internal class GetTableInfoInteractor(
    val source: Sources.Local.Pragma
) : Interactors.GetTableInfo {

    override suspend fun invoke(input: Query): QueryResult =
        source.getTableInfo(input)
}
