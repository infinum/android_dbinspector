package com.infinum.dbinspector.domain.schema.control.mappers

import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.Page

internal class SchemaMapper(
    private val cellMapper: Mappers.Cell
) : Mappers.Schema {

    override suspend fun invoke(model: QueryResult): Page =
        Page(
            beforeCount = model.beforeCount,
            afterCount = model.afterCount,
            cells = model.rows.map { row ->
                row.fields.toList().map { field -> cellMapper(field) }
            }.flatten(),
            nextPage = model.nextPage
        )
}
