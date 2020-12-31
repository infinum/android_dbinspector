package com.infinum.dbinspector.domain.shared.models.parameters

import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.domain.shared.models.Sort

internal data class SortParameters(
    val sort: Sort = Sort.ASCENDING
) : BaseParameters
