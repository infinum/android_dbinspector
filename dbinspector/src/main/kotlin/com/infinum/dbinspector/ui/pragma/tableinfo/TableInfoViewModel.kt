package com.infinum.dbinspector.ui.pragma.tableinfo

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.pragma.shared.PragmaSourceViewModel

internal class TableInfoViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    getTable: UseCases.GetTablePragma
) : PragmaSourceViewModel(openConnection, closeConnection, getTable) {

    override fun pragmaStatement(name: String) =
        Statements.Pragma.tableInfo(name)
}
