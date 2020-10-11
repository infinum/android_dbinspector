package im.dino.dbinspector.ui.pragma.tableinfo

import im.dino.dbinspector.data.source.legacy.TableInfoOperation
import im.dino.dbinspector.ui.pragma.shared.PragmaDataSourceOld

internal class TableInfoDataSource(
    path: String,
    name: String,
    pageSize: Int
) : PragmaDataSourceOld(path) {

    override val operation = lazyOf(TableInfoOperation(name, pageSize))
}
