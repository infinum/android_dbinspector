package com.infinum.dbinspector.domain.shared.models

internal data class Page(
    val nextPage: Int? = null,
    val beforeCount: Int = 0,
    val afterCount: Int = 0,
    val fields: List<String>
)
