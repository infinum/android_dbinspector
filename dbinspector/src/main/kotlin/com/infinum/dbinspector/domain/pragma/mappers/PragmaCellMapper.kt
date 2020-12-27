package com.infinum.dbinspector.domain.pragma.mappers

import com.infinum.dbinspector.data.models.local.cursor.Field
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.Cell

internal class PragmaCellMapper : Mappers.PragmaCell {

    override fun toDomain(field: Field): Cell =
        Cell(text = field.text)
}
