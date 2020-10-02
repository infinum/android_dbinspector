package im.dino.dbinspector.ui.databases

import im.dino.dbinspector.domain.database.models.Database

internal data class DatabaseInteractions(
    val onDelete: (Database) -> Unit,
    val onEdit: (Database) -> Unit,
    val onCopy: (Database) -> Unit,
    val onShare: (Database) -> Unit
)
