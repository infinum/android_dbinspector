package com.infinum.dbinspector.domain.pragma.mappers

import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page

internal class PragmaMapper : Mappers.Pragma {

    private val headerTransformer: ((String) -> Cell) = { Cell(text = it) }

    private val cellTransformer: ((Field) -> Cell) = { Cell(text = it.text) }

    override fun transformToHeader(): (String) -> Cell =
        headerTransformer

    override fun transformToCell(): (Field) -> Cell =
        cellTransformer

    override suspend fun invoke(model: QueryResult): Page =
        Page(
            cells = model.rows.map { row ->
                row.fields.toList().map(transform = cellTransformer)
            }.flatten(),
            nextPage = model.nextPage
        )
}
