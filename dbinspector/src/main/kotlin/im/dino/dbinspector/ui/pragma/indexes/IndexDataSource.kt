package im.dino.dbinspector.ui.pragma.indexes

import im.dino.dbinspector.data.source.legacy.IndexesOperation
import im.dino.dbinspector.ui.pragma.shared.PragmaDataSourceOld

internal class IndexDataSource(
    path: String,
    name: String,
    pageSize: Int
) : PragmaDataSourceOld(path) {

    override val operation = lazyOf(IndexesOperation(name, pageSize))
}
