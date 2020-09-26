package im.dino.dbinspector.ui.databases

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import im.dino.dbinspector.data.source.local.DatabaseManager
import im.dino.dbinspector.domain.database.VersionOperation
import im.dino.dbinspector.domain.database.models.Database
import im.dino.dbinspector.ui.shared.base.BaseViewModel

internal class DatabaseViewModel : BaseViewModel() {

    val databases: MutableLiveData<List<Database>> = MutableLiveData()

    fun find() {
        launch {
            databases.value = io {
                DatabaseManager.find().map {
                    Database(
                        absolutePath = it.absolutePath,
                        path = it.parentFile?.absolutePath.orEmpty(),
                        name = it.nameWithoutExtension,
                        extension = it.extension,
                        version = VersionOperation()(it.absolutePath, null)
                    )
                }
            }
        }
    }

    fun import(uris: List<Uri>) =
        launch {
            io {
                DatabaseManager.import(uris)
            }
            find()
        }

    fun remove(databaseFilename: String) =
        launch {
            val ok = io {
                DatabaseManager.remove(databaseFilename)
            }
            if (ok) {
                find()
            }
        }

    fun rename(databasePath: String, databaseFilename: String) =
        launch {
            val ok = io {
                DatabaseManager.rename(databasePath, databaseFilename)
            }
            if (ok) {
                find()
            }
        }

    fun copy(databaseAbsolutePath: String, databasePath: String, databaseName: String, databaseExtension: String) =
        launch {
            val ok = io {
                DatabaseManager.copy(
                    databaseAbsolutePath,
                    databasePath,
                    databaseName,
                    databaseExtension
                )
            }
            if (ok) {
                find()
            }
        }
}