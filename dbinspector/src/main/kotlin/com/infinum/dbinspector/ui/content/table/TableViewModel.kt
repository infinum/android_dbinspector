package com.infinum.dbinspector.ui.content.table

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.content.shared.ContentViewModel

internal class TableViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    tableInfo: UseCases.GetTableInfo,
    table: UseCases.GetTable,
    dropTableContent: UseCases.DropTableContent
) : ContentViewModel(
    openConnection,
    closeConnection,
    tableInfo,
    table,
    dropTableContent
) {

    override fun headerStatement(name: String) =
        Statements.Pragma.tableInfo(name)

    override fun schemaStatement(name: String, orderBy: String?, sort: Sort) =
        Statements.Schema.table(name, orderBy, sort)

    override fun dropStatement(name: String) =
        Statements.Schema.dropTableContent(name)
}
