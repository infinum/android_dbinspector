package im.dino.dbinspector.ui.pragma.indexes

import im.dino.dbinspector.domain.pragma.schema.IndexesOperation
import im.dino.dbinspector.ui.pragma.shared.PragmaDataSource

internal class IndexDataSource(
    path: String,
    name: String
) : PragmaDataSource(path) {

    override val operation = lazyOf(IndexesOperation(name))
}