package com.infinum.dbinspector.ui.pragma.foreignkeys

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.pragma.shared.PragmaSourceViewModel

internal class ForeignKeysViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    getForeignKeys: UseCases.GetForeignKeys
) : PragmaSourceViewModel(openConnection, closeConnection, getForeignKeys) {

    override fun pragmaStatement(name: String) =
        Statements.Pragma.foreignKeys(name)
}
