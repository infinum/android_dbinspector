package im.dino.dbinspector.ui.databases

import im.dino.dbinspector.domain.database.models.DatabaseDescriptor

internal data class DatabaseInteractions(
    val onDelete: (DatabaseDescriptor) -> Unit,
    val onEdit: (DatabaseDescriptor) -> Unit,
    val onCopy: (DatabaseDescriptor) -> Unit,
    val onShare: (DatabaseDescriptor) -> Unit
)
