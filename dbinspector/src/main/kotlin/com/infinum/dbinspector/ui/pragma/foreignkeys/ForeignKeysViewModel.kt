package com.infinum.dbinspector.ui.pragma.foreignkeys

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.pragma.shared.PragmaSourceViewModel

internal class ForeignKeysViewModel(
    private val getPragma: UseCases.GetForeignKeys
) : PragmaSourceViewModel() {

    override fun pragmaStatement(name: String) =
        Statements.Pragma.foreignKeys(name)

    override fun dataSource(databasePath: String, statement: String) =
        ForeignKeysDataSource(databasePath, statement, getPragma)
}
