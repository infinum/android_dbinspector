package com.infinum.dbinspector.domain.pragma.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.QueryResult
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.shared.models.Query

internal class GetForeignKeysInteractor(
    val source: Sources.Local.Pragma
) : Interactors.GetForeignKeys {

    override suspend fun invoke(input: Query): QueryResult =
        source.getForeignKeys(input)
}
