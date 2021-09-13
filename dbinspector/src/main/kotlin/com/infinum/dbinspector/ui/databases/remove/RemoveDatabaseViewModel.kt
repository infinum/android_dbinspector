package com.infinum.dbinspector.ui.databases.remove

import android.content.Context
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.ui.shared.base.BaseViewModel

internal class RemoveDatabaseViewModel(
    private val removeDatabase: UseCases.RemoveDatabase,
) : BaseViewModel<RemoveDatabaseState, Any>() {

    fun remove(context: Context, database: DatabaseDescriptor) =
        launch {
            val result = io {
                removeDatabase(
                    DatabaseParameters.Command(
                        context = context,
                        databaseDescriptor = database
                    )
                )
            }
            setState(
                RemoveDatabaseState.Removed(
                    success = result.isNotEmpty()
                )
            )
        }
}
