package com.infinum.dbinspector.ui.databases

import android.content.Context
import android.net.Uri
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.ui.shared.base.BaseViewModel

internal class DatabaseViewModel(
    private val getDatabases: UseCases.GetDatabases,
    private val importDatabases: UseCases.ImportDatabases,
    private val removeDatabase: UseCases.RemoveDatabase,
    private val copyDatabase: UseCases.CopyDatabase
) : BaseViewModel<DatabaseState, Any>() {

    fun browse(context: Context, query: String? = null) {
        launch {
            val result = io {
                getDatabases(
                    DatabaseParameters.Get(
                        context = context,
                        argument = query
                    )
                )
            }
            setState(DatabaseState.Databases(databases = result))
        }
    }

    fun import(context: Context, uris: List<Uri>) =
        launch {
            importDatabases(
                DatabaseParameters.Import(
                    context = context,
                    importUris = uris
                )
            )
            browse(context)
        }

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
            if (result.isNotEmpty()) {
                browse(context)
            }
        }

    fun copy(context: Context, database: DatabaseDescriptor) =
        launch {
            val ok = io {
                copyDatabase(
                    DatabaseParameters.Command(
                        context = context,
                        databaseDescriptor = database
                    )
                )
            }
            if (ok.isNotEmpty()) {
                browse(context)
            }
        }
}
