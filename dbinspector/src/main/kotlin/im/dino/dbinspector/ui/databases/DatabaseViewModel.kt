package im.dino.dbinspector.ui.databases

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.database.models.DatabaseDescriptor
import im.dino.dbinspector.domain.database.models.Operation
import im.dino.dbinspector.ui.shared.base.BaseViewModel
import im.dino.dbinspector.ui.shared.bus.EventBus
import im.dino.dbinspector.ui.shared.bus.models.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

internal class DatabaseViewModel(
    private val context: Context,
    private val getDatabases: UseCases.GetDatabases,
    private val importDatabases: UseCases.ImportDatabases,
    private val removeDatabase: UseCases.RemoveDatabase,
    private val copyDatabase: UseCases.CopyDatabase
) : BaseViewModel() {

    val databases: MutableLiveData<List<DatabaseDescriptor>> = MutableLiveData()

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun observe(action: suspend () -> Unit) =
        launch {
            io {
                EventBus.receive<Event.RefreshDatabases>().collectLatest { action() }
            }
        }

    fun browse() {
        launch {
            databases.value = io {
                getDatabases(
                    Operation(
                        context = context
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
