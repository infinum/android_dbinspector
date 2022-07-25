package com.infinum.dbinspector.ui.content.trigger

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.Statements
import com.infinum.dbinspector.ui.content.shared.ContentViewModel

internal class TriggerViewModel(
    openConnection: com.infinum.dbinspector.domain.UseCases.OpenConnection,
    closeConnection: com.infinum.dbinspector.domain.UseCases.CloseConnection,
    triggerInfo: com.infinum.dbinspector.domain.UseCases.GetTriggerInfo,
    trigger: com.infinum.dbinspector.domain.UseCases.GetTrigger,
    dropTrigger: com.infinum.dbinspector.domain.UseCases.DropTrigger
) : ContentViewModel(
    openConnection,
    closeConnection,
    triggerInfo,
    trigger,
    dropTrigger
) {

    override fun headerStatement(name: String) = ""

    override fun schemaStatement(name: String, orderBy: String?, sort: Sort) =
        Statements.Schema.trigger(name)

    override fun dropStatement(name: String) =
        Statements.Schema.dropTrigger(name)
}
