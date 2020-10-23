package com.infinum.dbinspector.ui.databases

import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor

internal data class DatabaseInteractions(
    val onDelete: (DatabaseDescriptor) -> Unit,
    val onEdit: (DatabaseDescriptor) -> Unit,
    val onCopy: (DatabaseDescriptor) -> Unit,
    val onShare: (DatabaseDescriptor) -> Unit
)
