package com.infinum.dbinspector.domain.shared.converters

import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.Sort

internal class SortConverter : Converters.Sort {

    override suspend fun invoke(parameters: Sort): Order =
        Order(parameters.rawValue)
}
