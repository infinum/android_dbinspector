package com.infinum.dbinspector.ui.databases

import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor

internal sealed class DatabaseState {

    data class Databases(
        val databases: List<DatabaseDescriptor>
    ) : DatabaseState()
}
