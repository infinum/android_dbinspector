package com.infinum.dbinspector.ui.databases.rename

internal sealed class RenameDatabaseState {

    data class Renamed(
        val success: Boolean
    ) : RenameDatabaseState()
}
