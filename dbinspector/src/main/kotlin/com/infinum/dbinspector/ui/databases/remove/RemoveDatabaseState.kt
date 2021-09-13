package com.infinum.dbinspector.ui.databases.remove

internal sealed class RemoveDatabaseState {

    data class Removed(
        val success: Boolean
    ) : RemoveDatabaseState()
}
