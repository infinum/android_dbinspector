package im.dino.dbinspector.ui.shared.headers

import im.dino.dbinspector.domain.shared.models.Direction

data class Header(
    val active: Boolean = false,
    val name: String,
    val direction: Direction = Direction.ASCENDING
)
