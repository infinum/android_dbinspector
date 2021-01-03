package com.infinum.dbinspector.domain.raw.control.converters

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters

internal class RawQueryConverter(
    private val sortConverter: Converters.Sort
) : Converters.RawQuery {

    override suspend fun invoke(parameters: ContentParameters): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.database,
            statement = parameters.statement,
            order = sortConverter(parameters.sort),
            pageSize = parameters.pageSize,
            page = parameters.page
        )
}
