package im.dino.dbinspector.ui.pragma.indexes

import im.dino.dbinspector.domain.pragma.schema.IndexesOperation
import im.dino.dbinspector.ui.pragma.shared.PragmaDataSource

internal class IndexDataSource(
    path: String,
    private val name: String
) : PragmaDataSource(path) {

    override fun operation() = lazy {
        IndexesOperation(name)
    }
}