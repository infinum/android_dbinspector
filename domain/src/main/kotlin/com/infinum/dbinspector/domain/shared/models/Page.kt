package com.infinum.dbinspector.domain.shared.models

public data class Page(
    val nextPage: Int? = null,
    val beforeCount: Int = 0,
    val afterCount: Int = 0,
    val cells: List<Cell>
)
