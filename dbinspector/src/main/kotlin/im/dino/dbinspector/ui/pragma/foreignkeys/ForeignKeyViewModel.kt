package im.dino.dbinspector.ui.pragma.foreignkeys

import im.dino.dbinspector.ui.pragma.shared.PragmaViewModel

internal class ForeignKeyViewModel : PragmaViewModel() {

    override fun source(path: String, name: String) =
        ForeignKeyDataSource(path, name, PAGE_SIZE)
}
