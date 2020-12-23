package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.Field
import com.infinum.dbinspector.domain.shared.models.Cell

internal interface BaseCellMapper {

    fun toDomain(field: Field): Cell
}
