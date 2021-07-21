package com.infinum.dbinspector.ui.databases.edit

import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor

internal sealed class EditDatabaseEvent {

    data class Renamed(
        val descriptor: DatabaseDescriptor
    ) : EditDatabaseEvent()
}
