package com.infinum.dbinspector.ui.content.view

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Direction
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.content.shared.ContentViewModel

internal class ViewViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    tableInfo: UseCases.GetTableInfo,
    private val view: UseCases.GetView,
    dropView: UseCases.DropView
) : ContentViewModel(
    openConnection,
    closeConnection,
    tableInfo,
    dropView
) {

    override fun headerStatement(name: String) =
        Statements.Pragma.tableInfo(name)

    override fun schemaStatement(name: String, orderBy: String?, direction: Direction) =
        Statements.Schema.view(name)

    override fun dropStatement(name: String) =
        Statements.Schema.dropView(name)

    override fun dataSource(databasePath: String, statement: String) =
        ViewDataSource(databasePath, statement, view)
}
