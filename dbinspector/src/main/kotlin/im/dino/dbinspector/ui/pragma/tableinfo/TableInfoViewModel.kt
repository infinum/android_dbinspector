package im.dino.dbinspector.ui.pragma.tableinfo

import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.pragma.shared.PragmaSourceViewModel

internal class TableInfoViewModel(
    private val getPragma: UseCases.GetTablePragma
) : PragmaSourceViewModel() {

    override fun pragmaStatement(name: String) =
        Statements.Pragma.tableInfo(name)

    override fun dataSource(databasePath: String, statement: String) =
        TableInfoDataSource(databasePath, statement, getPragma)
}
