package com.infinum.dbinspector.ui.shared.headers

import com.infinum.dbinspector.data.models.local.cursor.Direction

data class Header(
    val active: Boolean = false,
    val name: String,
    val direction: Direction = Direction.ASCENDING
)
