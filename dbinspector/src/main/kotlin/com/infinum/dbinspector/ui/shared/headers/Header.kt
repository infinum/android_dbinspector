package com.infinum.dbinspector.ui.shared.headers

import com.infinum.dbinspector.domain.shared.models.Direction

data class Header(
    val active: Boolean = false,
    val name: String,
    val direction: Direction = Direction.ASCENDING
)
