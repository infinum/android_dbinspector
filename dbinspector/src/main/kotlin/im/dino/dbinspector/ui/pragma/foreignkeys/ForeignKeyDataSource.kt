package im.dino.dbinspector.ui.pragma.foreignkeys

import im.dino.dbinspector.domain.pragma.schema.ForeignKeysOperation
import im.dino.dbinspector.ui.pragma.shared.PragmaDataSource

internal class ForeignKeyDataSource(
    path: String,
    name: String,
    pageSize: Int
) : PragmaDataSource(path) {

    override val operation = lazyOf(ForeignKeysOperation(name, pageSize))
}
