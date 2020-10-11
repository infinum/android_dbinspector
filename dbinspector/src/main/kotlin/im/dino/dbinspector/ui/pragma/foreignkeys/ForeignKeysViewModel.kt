package im.dino.dbinspector.ui.pragma.foreignkeys

import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.pragma.shared.PragmaSourceViewModel

internal class ForeignKeysViewModel(
    private val getPragma: UseCases.GetForeignKeys
) : PragmaSourceViewModel() {

    override fun pragmaStatement(name: String) =
        Statements.Pragma.foreignKeys(name)

    override fun dataSource(databasePath: String, statement: String) =
        ForeignKeysDataSource(databasePath, statement, getPragma)
}
