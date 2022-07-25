package com.infinum.dbinspector.ui.pragma.indexes

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.pragma.shared.PragmaSourceViewModel

internal class IndexViewModel(
    openConnection: com.infinum.dbinspector.domain.UseCases.OpenConnection,
    closeConnection: com.infinum.dbinspector.domain.UseCases.CloseConnection,
    getIndexes: com.infinum.dbinspector.domain.UseCases.GetIndexes
) : PragmaSourceViewModel(openConnection, closeConnection, getIndexes) {

    override fun pragmaStatement(name: String) =
        Statements.Pragma.indexes(name)
}
