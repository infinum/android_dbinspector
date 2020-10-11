package im.dino.dbinspector.ui.pragma.foreignkeys

import im.dino.dbinspector.data.source.legacy.ForeignKeysOperation
import im.dino.dbinspector.ui.pragma.shared.PragmaDataSourceOld

internal class ForeignKeyDataSource(
    path: String,
    name: String,
    pageSize: Int
) : PragmaDataSourceOld(path) {

    override val operation = lazyOf(ForeignKeysOperation(name, pageSize))
}
