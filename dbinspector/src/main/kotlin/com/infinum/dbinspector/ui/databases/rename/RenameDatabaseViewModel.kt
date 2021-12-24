package com.infinum.dbinspector.ui.databases.rename

import android.content.Context
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.ui.shared.base.BaseViewModel

internal class RenameDatabaseViewModel(
    private val renameDatabase: UseCases.RenameDatabase
) : BaseViewModel<RenameDatabaseState, Any>() {

    fun rename(
        context: Context,
        database: DatabaseDescriptor,
        newName: String
    ) =
        launch {
            val result = io {
                renameDatabase(
                    DatabaseParameters.Rename(
                        context = context,
                        databaseDescriptor = database,
                        argument = newName
                    )
                )
            }
            if (result.isNotEmpty()) {
                setState(RenameDatabaseState.Renamed(success = result.firstOrNull()?.name.equals(newName)))
            } else {
                setError(IllegalStateException("Rename failed."))
            }
        }
}
