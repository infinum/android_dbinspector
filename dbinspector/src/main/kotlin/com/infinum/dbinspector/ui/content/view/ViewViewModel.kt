package com.infinum.dbinspector.ui.content.view

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.content.shared.ContentViewModel

internal class ViewViewModel(
    openConnection: com.infinum.dbinspector.domain.UseCases.OpenConnection,
    closeConnection: com.infinum.dbinspector.domain.UseCases.CloseConnection,
    tableInfo: com.infinum.dbinspector.domain.UseCases.GetTableInfo,
    view: com.infinum.dbinspector.domain.UseCases.GetView,
    dropView: com.infinum.dbinspector.domain.UseCases.DropView
) : ContentViewModel(
    openConnection,
    closeConnection,
    tableInfo,
    view,
    dropView
) {
    override fun headerStatement(name: String) =
        Statements.Pragma.tableInfo(name)

    override fun schemaStatement(name: String, orderBy: String?, sort: Sort) =
        Statements.Schema.view(name)

    override fun dropStatement(name: String) =
        Statements.Schema.dropView(name)
}
