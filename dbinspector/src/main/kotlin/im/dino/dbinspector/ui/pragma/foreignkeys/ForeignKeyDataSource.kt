package im.dino.dbinspector.ui.pragma.foreignkeys

import im.dino.dbinspector.domain.pragma.schema.ForeignKeysOperation
import im.dino.dbinspector.ui.pragma.shared.PragmaDataSource

internal class ForeignKeyDataSource(
    path: String,
    private val name: String
) : PragmaDataSource(path) {

    override fun operation() = lazy {
        ForeignKeysOperation(name)
    }
}