package com.infinum.dbinspector.domain.raw.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Interactors

internal class GetAffectedRowsInteractor(
    val source: Sources.Local.RawQuery
) : Interactors.GetAffectedRows {

    override suspend fun invoke(input: Query): QueryResult =
        source.affectedRows(input)
}
