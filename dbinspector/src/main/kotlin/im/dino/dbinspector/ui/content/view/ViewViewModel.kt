package im.dino.dbinspector.ui.content.view

import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Direction
import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.content.shared.ContentViewModel

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
