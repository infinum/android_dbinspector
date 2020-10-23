package com.infinum.dbinspector.ui.databases

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.ui.shared.base.BaseViewModel

internal class DatabaseViewModel(
    private val context: Context,
    private val getDatabases: UseCases.GetDatabases,
    private val importDatabases: UseCases.ImportDatabases,
    private val removeDatabase: UseCases.RemoveDatabase,
    private val copyDatabase: UseCases.CopyDatabase
) : BaseViewModel() {

    val databases: MutableLiveData<List<DatabaseDescriptor>> = MutableLiveData()

    fun browse(query: String? = null) {
        launch {
            databases.value = io {
                getDatabases(
                    Operation(
                        context = context,
                        argument = query
                    )
                )
            }
        }
    }

    fun import(uris: List<Uri>) =
        launch {
            importDatabases(
                Operation(
                    context = context,
                    importUris = uris
                )
            )
            browse()
        }

    fun remove(database: DatabaseDescriptor) =
        launch {
            val result = io {
                removeDatabase(
                    Operation(
                        context = context,
                        databaseDescriptor = database
                    )
                )
            }
            if (result.isNotEmpty()) {
                browse()
            }
        }

    fun copy(database: DatabaseDescriptor) =
        launch {
            val ok = io {
                copyDatabase(
                    Operation(
                        context = context,
                        databaseDescriptor = database
                    )
                )
            }
            if (ok.isNotEmpty()) {
                browse()
            }
        }
}
