package com.infinum.dbinspector.ui.content.table

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Direction
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.content.shared.ContentViewModel

internal class TableViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    tableInfo: UseCases.GetTableInfo,
    private val table: UseCases.GetTable,
    dropTableContent: UseCases.DropTableContent
) : ContentViewModel(
    openConnection,
    closeConnection,
    tableInfo,
    dropTableContent
) {

    override fun headerStatement(name: String) =
        Statements.Pragma.tableInfo(name)

    override fun schemaStatement(name: String, orderBy: String?, direction: Direction) =
        Statements.Schema.table(name, orderBy, direction)

    override fun dropStatement(name: String) =
        Statements.Schema.dropTableContent(name)

    override fun dataSource(databasePath: String, statement: String) =
        TableDataSource(databasePath, statement, table)
}
