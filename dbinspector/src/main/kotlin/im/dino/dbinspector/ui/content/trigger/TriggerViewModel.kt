package im.dino.dbinspector.ui.content.trigger

import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Direction
import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.content.shared.ContentViewModel

internal class TriggerViewModel(
    openConnection: UseCases.OpenConnection,
    closeConnection: UseCases.CloseConnection,
    triggerInfo: UseCases.GetTriggerInfo,
    private val trigger: UseCases.GetTrigger,
    dropTrigger: UseCases.DropTrigger
) : ContentViewModel(
    openConnection,
    closeConnection,
    triggerInfo,
    dropTrigger
) {

    override fun headerStatement(name: String) = ""

    override fun schemaStatement(name: String, orderBy: String?, direction: Direction) =
        Statements.Schema.trigger(name)

    override fun dropStatement(name: String) =
        Statements.Schema.dropTrigger(name)

    override fun dataSource(databasePath: String, statement: String) =
        TriggerDataSource(databasePath, statement, trigger)
}
