package com.infinum.dbinspector.ui.pragma.tableinfo

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.pragma.shared.PragmaSourceViewModel

internal class TableInfoViewModel(
    private val getPragma: UseCases.GetTablePragma
) : PragmaSourceViewModel() {

    override fun pragmaStatement(name: String) =
        Statements.Pragma.tableInfo(name)

    override fun dataSource(databasePath: String, statement: String) =
        TableInfoDataSource(databasePath, statement, getPragma)
}
