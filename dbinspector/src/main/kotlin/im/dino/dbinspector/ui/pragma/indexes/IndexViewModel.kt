package im.dino.dbinspector.ui.pragma.indexes

import im.dino.dbinspector.ui.pragma.shared.PragmaViewModel

internal class IndexViewModel : PragmaViewModel() {

    override fun source(path: String, name: String) =
        IndexDataSource(path, name, PAGE_SIZE)
}