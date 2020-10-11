package im.dino.dbinspector.ui.databases.edit

import android.content.Context
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.database.models.DatabaseDescriptor
import im.dino.dbinspector.domain.database.models.Operation
import im.dino.dbinspector.ui.shared.base.BaseViewModel

internal class EditViewModel(
    private val context: Context,
    private val renameDatabase: UseCases.RenameDatabase
) : BaseViewModel() {

    fun rename(database: DatabaseDescriptor, newName: String, action: suspend (DatabaseDescriptor) -> Unit) =
        launch {
            val result = io {
                renameDatabase(
                    Operation(
                        context = context,
                        databaseDescriptor = database,
                        argument = newName
                    )
                )
            }
            if (result.isNotEmpty()) {
                action(result.first())
            }
        }
}
