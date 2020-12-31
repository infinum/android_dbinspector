package com.infinum.dbinspector.ui.shared.headers

import com.infinum.dbinspector.domain.shared.models.Sort

data class Header(
    val active: Boolean = false,
    val name: String,
    val sort: Sort = Sort.ASCENDING
)
