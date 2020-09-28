package im.dino.dbinspector.ui.pragma.database

import im.dino.dbinspector.data.source.local.DatabaseManager
import im.dino.dbinspector.ui.shared.base.BaseViewModel

internal class EditViewModel : BaseViewModel() {

    fun rename(databasePath: String, databaseFilename: String, action: suspend () -> Unit) =
        launch {
            val ok = io {
                DatabaseManager.rename(databasePath, databaseFilename)
            }
            if (ok) {
                action()
            }
        }
}