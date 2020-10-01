package im.dino.dbinspector.ui.pragma.tableinfo

import im.dino.dbinspector.ui.pragma.shared.PragmaViewModel

internal class TableInfoViewModel : PragmaViewModel() {

    override fun source(path: String, name: String) =
        TableInfoDataSource(path, name)
}