package im.dino.dbinspector.ui.databases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import im.dino.dbinspector.data.source.local.DatabaseFinder
import im.dino.dbinspector.domain.database.VersionOperation
import im.dino.dbinspector.domain.database.models.Database

class DatabaseViewModel : ViewModel() {

    val databases: MutableLiveData<List<Database>> = MutableLiveData()

    fun find() {
        DatabaseFinder.find()
        databases.value = DatabaseFinder.get().map {
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