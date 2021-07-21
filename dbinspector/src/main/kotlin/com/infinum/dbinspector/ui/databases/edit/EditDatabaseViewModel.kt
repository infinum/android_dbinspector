package com.infinum.dbinspector.ui.databases.edit

import android.content.Context
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.ui.shared.base.BaseViewModel

internal class EditDatabaseViewModel(
    private val renameDatabase: UseCases.RenameDatabase
) : BaseViewModel<Any, EditDatabaseEvent>() {

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
                emitEvent(EditDatabaseEvent.Renamed(descriptor = result.first()))
            }
        }
}
