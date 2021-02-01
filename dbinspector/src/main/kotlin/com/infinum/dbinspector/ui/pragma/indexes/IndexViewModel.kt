package com.infinum.dbinspector.ui.pragma.indexes

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.pragma.shared.PragmaSourceViewModel

internal class IndexViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    getIndexes: UseCases.GetIndexes
) : PragmaSourceViewModel(openConnection, closeConnection, getIndexes) {

    override fun pragmaStatement(name: String) =
        Statements.Pragma.indexes(name)
}
