package com.infinum.dbinspector.ui.pragma.indexes

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.pragma.shared.PragmaSourceViewModel

internal class IndexViewModel(
    private val getPragma: UseCases.GetIndexes
) : PragmaSourceViewModel() {

    override fun pragmaStatement(name: String) =
        Statements.Pragma.indexes(name)

    override fun dataSource(databasePath: String, statement: String) =
        IndexDataSource(databasePath, statement, getPragma)
}
