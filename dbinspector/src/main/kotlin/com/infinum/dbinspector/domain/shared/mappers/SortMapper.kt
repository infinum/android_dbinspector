package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.Sort

internal class SortMapper : Mappers.Sort {

    override fun mapDomainToLocal(model: Sort): Order =
        Order(model.rawValue)
}
