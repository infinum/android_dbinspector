package im.dino.dbinspector.ui.content.table

import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.pragma.models.TableInfoColumns
import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.content.shared.ContentViewModel

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

    override fun schemaStatement(name: String) =
        Statements.Schema.table(name)

    override fun dropStatement(name: String) =
        Statements.Schema.dropTableContent(name)

    override fun dataSource(databasePath: String, statement: String) =
        TableDataSource(databasePath, statement, table)
}
