package im.dino.dbinspector.ui.pragma.tableinfo

import im.dino.dbinspector.domain.pragma.schema.TableInfoOperation
import im.dino.dbinspector.ui.pragma.shared.PragmaDataSource

internal class TableInfoDataSource(
    path: String,
    name: String
) : PragmaDataSource(path) {

    override val operation = lazyOf(TableInfoOperation(name))
}